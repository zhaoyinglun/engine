package com.ruoyi.netty.handler;

import com.ruoyi.project.system.domain.HkAdsbBase;
import com.ruoyi.project.system.domain.HkAdsbIdent;
import com.ruoyi.project.system.domain.HkAdsbPostion;
import com.ruoyi.project.system.domain.HkAdsbSpeed;
import com.ruoyi.project.system.hbasedao.HkFlightDao;
import com.ruoyi.project.system.hbasedao.HkGeohashDao;
import com.ruoyi.project.system.hbasedao.HkPlaneDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.mapreduce.ID;
import org.opensky.libadsb.ModeSDecoder;
import org.opensky.libadsb.Position;
import org.opensky.libadsb.exceptions.BadFormatException;
import org.opensky.libadsb.exceptions.UnspecifiedFormatError;
import org.opensky.libadsb.msgs.*;
import org.opensky.libadsb.tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by root on 5/9/20.
 */
@Component
@Slf4j
public class HkAdsbDecoder {

    @Autowired
    private ModeSDecoder decoder;

    @Autowired
    private HkPlaneDao planeDao;

    @Autowired
    private HkGeohashDao geohashDao;

    @Autowired
    private HkFlightDao flightDao;

    private Map<String, HkAdsbPostion> postionMap = new HashMap<>();

    private DoubleBuffer doubleBuffer = new DoubleBuffer();

    private int cntMsg = 0;

    private long lastFlushTime = 0;

    public void decodeMsg(long timestamp, String raw, Position receiverPos) {
        ModeSReply msg;
        try {
            msg = decoder.decode(raw);
        } catch (BadFormatException e) {
            log.error(":Malformed message! Skipping it. Message: " + e.getMessage());
            return;
        } catch (UnspecifiedFormatError e) {
            log.error(":Unspecified message! Skipping it...");
            return;
        }
        cntMsg++;
        String icao24 = tools.toHexString(msg.getIcao24());
        // check for erroneous messages; some receivers set
        // parity field to the result of the CRC polynomial division
        if (tools.isZero(msg.getParity()) || msg.checkParity()) { // CRC is ok
            switch (msg.getType()) {
                case ADSB_AIRBORN_POSITION_V0:
                case ADSB_AIRBORN_POSITION_V1:
                case ADSB_AIRBORN_POSITION_V2:
                    doAirPosDecode(timestamp, (AirbornePositionV0Msg) msg, receiverPos);
                    break;
                case ADSB_SURFACE_POSITION_V0:
                case ADSB_SURFACE_POSITION_V1:
                case ADSB_SURFACE_POSITION_V2:
                    doSurfacePosDecode(timestamp, (SurfacePositionV0Msg)msg, receiverPos);
                    break;
                case ADSB_EMERGENCY:
                    break;
                case ADSB_AIRSPEED:
                    doSpeedDecode(timestamp, (AirspeedHeadingMsg) msg);
                    break;
                case ADSB_IDENTIFICATION:
                    doIdentificationDecode(timestamp, (IdentificationMsg) msg);
                    break;
                case ADSB_STATUS_V0:
                case ADSB_AIRBORN_STATUS_V1:
                case ADSB_AIRBORN_STATUS_V2:
                case ADSB_SURFACE_STATUS_V1:
                case ADSB_SURFACE_STATUS_V2:
                case ADSB_TCAS:
                    break;
                case ADSB_VELOCITY:
                    doVolecityDecode(timestamp, (VelocityOverGroundMsg)msg);
                    break;
                case EXTENDED_SQUITTER:
                    log.error("[" + icao24 + "]: Unknown extended squitter with type code " + ((ExtendedSquitter) msg).getFormatTypeCode() + "!");
                    break;
            }
        }else if (msg.getDownlinkFormat() != 17) { // CRC failed
            switch (msg.getType()) {
                case MODES_REPLY: break;
                case SHORT_ACAS:
                case ALTITUDE_REPLY:
                case IDENTIFY_REPLY:
                case ALL_CALL_REPLY:
                case LONG_ACAS:
                case MILITARY_EXTENDED_SQUITTER:
                case COMM_B_ALTITUDE_REPLY:
                case COMM_B_IDENTIFY_REPLY:
                case COMM_D_ELM:
                    break;
                default:
            }
        }else{
            log.warn(String.format("Message contains biterrors:[%s:%s]", icao24, raw));
        }
    }

    public void doAirPosDecode(long timestamp, AirbornePositionV0Msg ap0, Position receiverPos){
        Position c0 = decoder.decodePosition(timestamp, ap0, receiverPos);
        if(c0 != null){
            HkAdsbPostion pos = new HkAdsbPostion(c0.getLatitude(), c0.getLongitude(), c0.getAltitude().floatValue(), false);
            pos.setTimestamp(timestamp);
            pos.setIcao(tools.toHexString(ap0.getIcao24()));
            if (ap0.hasAltitude() && !ap0.isBarometricAltitude())
                pos.setAltitude(tools.feet2Meters(ap0.getAltitude()).floatValue());

            Integer geoMinusBaro = decoder.getGeoMinusBaro(ap0);
            if (ap0.hasAltitude() && ap0.isBarometricAltitude() && geoMinusBaro != null) {
                pos.setAltitude(tools.feet2Meters(ap0.getAltitude() + geoMinusBaro).floatValue());
            }
            log.debug(String.format("%d:开始保存空中位置消息:%s", cntMsg, pos.toString()));
            postionMap.put(pos.getIcao(), pos);
            saveToBuffer(pos);
        }
    }

    public void doSurfacePosDecode(long timestamp, SurfacePositionV0Msg sp0, Position receiverPos){
        Position c0 = decoder.decodePosition(timestamp, sp0, receiverPos);
        if(c0 != null){
            HkAdsbPostion pos = new HkAdsbPostion(c0.getLatitude(), c0.getLongitude(), c0.getAltitude().floatValue(), true);
            pos.setTimestamp(timestamp);
            pos.setIcao(tools.toHexString(sp0.getIcao24()));
            if (sp0.hasValidHeading())
                pos.setTrack(sp0.getHeading().floatValue());
            if (sp0.hasGroundSpeed()) {
                pos.setGspeed(sp0.getGroundSpeed().floatValue());
            }
            log.debug("开始保存地面空中位置消息:" + pos.toString());
            postionMap.put(pos.getIcao(), pos);
            saveToBuffer(pos);
        }
    }

    /**
     * type=19 subtype=3,4 // 空速
     * @param timestamp
     * @param spdmsg
     */
    public void doSpeedDecode(long timestamp, AirspeedHeadingMsg spdmsg){

//        System.out.println("          Heading: " + airspeed.getHeading() + "° relative to " +
//                (airspeed.hasHeadingStatusFlag() ? "magnetic north" : "true north"));
        HkAdsbSpeed adsbSpeed = new HkAdsbSpeed();
        adsbSpeed.setTimestamp(timestamp);
        adsbSpeed.setIcao(tools.toHexString(spdmsg.getIcao24()));
        if(spdmsg.hasHeadingStatusFlag()) {
            adsbSpeed.setTrack(spdmsg.getHeading().floatValue());
        }
        if(spdmsg.hasVerticalRateInfo()){
            adsbSpeed.setVspeed(spdmsg.getVerticalRate()); // feet/min
        }
        adsbSpeed.setVspeed(spdmsg.getAirspeed());
        HkAdsbPostion lastpos = postionMap.get(adsbSpeed.getIcao());
        if(lastpos != null && lastpos.getTimestamp()+5000 > timestamp){
            // 速度报文与位置报文时差小于5秒，则将速度信息合并放入与经纬度同一条数据库记录
            adsbSpeed.setTimestamp(lastpos.getTimestamp());
            adsbSpeed.setLat(lastpos.getLatitude());
            adsbSpeed.setLon(lastpos.getLongitude());
        }
        saveToBuffer(adsbSpeed);
    }

    public void doIdentificationDecode(long timestamp, IdentificationMsg imsg){
        HkAdsbIdent ident = new HkAdsbIdent();
        String callsign = new String(imsg.getIdentity());
        String desc = imsg.getCategoryDescription();
        ident.setCallsign(callsign);
        ident.setTimestamp(timestamp);
        ident.setIcao(tools.toHexString(imsg.getIcao24()));
        saveToBuffer(ident);
    }

    /**
     * type=19 subtype=1,2 // 地速
     * @param timestamp
     * @param vmsg
     */
    public void doVolecityDecode(long timestamp, VelocityOverGroundMsg vmsg){
        HkAdsbSpeed adsbSpeed = new HkAdsbSpeed();
        adsbSpeed.setTimestamp(timestamp);
        adsbSpeed.setIcao(tools.toHexString(vmsg.getIcao24()));

        if(vmsg.hasVelocityInfo()){
            adsbSpeed.setGspeed(vmsg.getVelocity().floatValue());
        }
        if(vmsg.hasVerticalRateInfo()){
            adsbSpeed.setVspeed(vmsg.getVerticalRate());
        }
        if(vmsg.getHeading() != null){
            adsbSpeed.setTrack(vmsg.getHeading().floatValue());
        }
        HkAdsbPostion lastpos = postionMap.get(adsbSpeed.getIcao());
        if(lastpos != null && lastpos.getTimestamp()+5000 > timestamp){
            // 速度报文与位置报文时差小于5秒，则将速度信息合并放入与经纬度同一条数据库记录
            adsbSpeed.setTimestamp(lastpos.getTimestamp());
            adsbSpeed.setLat(lastpos.getLatitude());
            adsbSpeed.setLon(lastpos.getLongitude());
        }
        saveToBuffer(adsbSpeed);
    }

    public void saveToBuffer(HkAdsbBase hkAdsbBase){
        while(doubleBuffer.getCurLen() >= 10000) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (this){
            doubleBuffer.putToList(hkAdsbBase);
        }
    }

    /**
     * 通过线程执行刷写数据库的操作
     * 当上一次刷写在1000毫秒之前或者当前链表数据超过1000则刷写数据库
     */
    @Async
    public void flush(){
        for(;;) {

            if(doubleBuffer.isInFlush() || doubleBuffer.getCurLen() == 0){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            /*if (System.currentTimeMillis() - lastFlushTime < 1000 && doubleBuffer.getCurLen() < 1000) {
                continue;
            }*/

            synchronized (this) {
                if(doubleBuffer.isInFlush())
                    continue;
                /*if (System.currentTimeMillis() - lastFlushTime < 1000 && doubleBuffer.getCurLen() < 1000) {
                    continue;
                }*/
                doubleBuffer.setInFlush(true);
                doubleBuffer.exchange();
                lastFlushTime = System.currentTimeMillis();
            }
            try {
                doubleBuffer.flushToDb();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            synchronized (this){
                doubleBuffer.setInFlush(false);
            }
        }
    }

    @Async
    public void flushIfPending(){
        if(doubleBuffer.isInFlush() || doubleBuffer.getCurLen() == 0)
            return;

        synchronized (this){
            if(doubleBuffer.isInFlush())
                return;
            doubleBuffer.setInFlush(true);
            doubleBuffer.exchange();
            lastFlushTime = System.currentTimeMillis();
        }
        try {
            doubleBuffer.flushToDb();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        synchronized (this){
            doubleBuffer.setInFlush(false);
        }
    }

    /**
     * 因为刷写数据库比较慢，为了避免数据接收受到刷写的阻塞，
     * 采用双缓冲机制，解析后的数据放入curBuf，达到刷写条件时将curBuf和syncBuf做交换
     * 交换后将syncBuf刷写入数据库，并且清空syncBuf。刷写可采用多线程来提高效率
     */
    private class DoubleBuffer{
        private LinkedList<HkAdsbBase> curBuf = new LinkedList<>();
        private LinkedList<HkAdsbBase> syncBuf = new LinkedList<>();
        private boolean inFlush = false;
        private boolean isFull = false;

        public void putToList(HkAdsbBase hkAdsbBase){
            curBuf.add(hkAdsbBase);
        }

        public void flushToDb(){
            List<Put> hashList = new ArrayList<>();
            List<Put> planeList = new ArrayList<>();
            List<Put> flightList = new ArrayList<>();
            System.out.println("--->syncBuf.size=" + syncBuf.size());
            for(HkAdsbBase hkAdsbBase: syncBuf){
                if(hkAdsbBase instanceof HkAdsbPostion){
                    hashList.add(geohashDao.convertPosition((HkAdsbPostion)hkAdsbBase));
                    planeList.add(planeDao.convertPosition((HkAdsbPostion)hkAdsbBase));
                } else if(hkAdsbBase instanceof HkAdsbSpeed){
                    HkAdsbSpeed spd = (HkAdsbSpeed) hkAdsbBase;
                    if(spd.getLat() != null && spd.getLon() != null){
                        hashList.add(geohashDao.convertSpeed(spd));
                        planeList.add(planeDao.convertSpeed(spd));
                    }
                } else if(hkAdsbBase instanceof HkAdsbIdent){
                    Put put = flightDao.convertIdent((HkAdsbIdent) hkAdsbBase);
                    if(put != null)
                        flightList.add(put);
                }
            }
            if(hashList.size() > 0)
                geohashDao.putPuts(hashList);
            if(planeList.size() > 0)
                planeDao.putList(planeList);
            if(flightList.size() > 0)
                flightDao.putList(flightList);
            syncBuf.clear();
        }

        public void exchange(){
            LinkedList<HkAdsbBase> tmp = curBuf;
            curBuf = syncBuf;
            syncBuf = tmp;
        }

        public int getCurLen(){
            return curBuf.size();
        }

        public boolean isInFlush() {
            return inFlush;
        }

        public void setInFlush(boolean inFlush) {
            this.inFlush = inFlush;
        }

        public boolean isFull() {
            return isFull;
        }

        public void setFull(boolean full) {
            isFull = full;
        }
    }
}
