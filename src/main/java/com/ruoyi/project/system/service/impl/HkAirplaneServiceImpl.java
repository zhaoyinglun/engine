package com.ruoyi.project.system.service.impl;

import ch.hsr.geohash.GeoHash;
import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.utils.*;
import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.netty.handler.HkAdsbDecoder;
import com.ruoyi.project.system.domain.*;
import com.ruoyi.project.system.domain.vo.AirplaneEntityVo;
import com.ruoyi.project.system.domain.vo.Point;
import com.ruoyi.project.system.hbasedao.HkFlightDao;
import com.ruoyi.project.system.hbasedao.HkGeohashDao;
import com.ruoyi.project.system.hbasedao.HkPlaneDao;
import com.ruoyi.project.system.service.IHkAirplaneService;
import com.ruoyi.project.system.service.IHkAirportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by root on 4/26/20.
 */
@Slf4j
public class HkAirplaneServiceImpl implements IHkAirplaneService {

    private Connection connection;

    @Autowired
    private HkPlaneDao planeDao;

    @Autowired
    private HkGeohashDao geohashDao;

    @Autowired
    private HkFlightDao flightDao;

    @Autowired
    private IHkAirportService hkAirportService;

    @Autowired
    private HkAdsbDecoder decoder;

    public HkAirplaneServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public HkFlightTrack findCurrentTrackByIcao(String icao) {
        return null;
    }

    @Override
    public HkFlightTrack findTrackByIcao(String icao, long startTime) {
        return null;
    }

    @Override
    public List<AirplaneEntityVo> findPlaneByArea(double slon, double slat, double elon, double elat) {
        long ts = DateUtils.getNowDate().getTime();
        return findHisPlaneByArea(slon, slat, elon, elat, ts);
    }

    @Override
    public List<AirplaneEntityVo> findHisPlaneByArea(double slon, double slat, double elon, double elat, long time) {
        IPointFilter filter = new RectPointFilter(slat, slon, elat, elon);
        return findHisPlaneByArea(slon, slat, elon, elat, time, filter);
    }

    private List<AirplaneEntityVo> findHisPlaneByArea(double slon, double slat, double elon, double elat, long time, IPointFilter filter) {
        List<AirplaneEntityVo> results = new ArrayList<>();
        Map<String, AirplaneEntityVo> map = null;
        List<String> hashes = HkjsUtils.splitArea(slon, slat, elon, elat);
        try {
            if (hashes != null) {
                if (hashes.size() == 2) {
                    map = geohashDao.findByArea(time, hashes.get(0), hashes.get(1));
                } else if (hashes.size() == 4) {
                    map = geohashDao.findByArea(time, hashes.get(0), hashes.get(1));
                    map.putAll(geohashDao.findByArea(time, hashes.get(2), hashes.get(3)));
                } else if (hashes.size() == 8) {
                    map = geohashDao.findByArea(time, hashes.get(0), hashes.get(1));
                    map.putAll(geohashDao.findByArea(time, hashes.get(2), hashes.get(3)));
                    map.putAll(geohashDao.findByArea(time, hashes.get(4), hashes.get(5)));
                    map.putAll(geohashDao.findByArea(time, hashes.get(6), hashes.get(7)));
                }
            } else {
                String start = HkjsUtils.geohash(slat, slon);
                String end = HkjsUtils.geohash(elat, elon);
                map = geohashDao.findByTimeFilter(time, start, end);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IOException in findHisPlaneByArea:", e.getMessage());
        }

        if (map != null) {
            results = map.values().stream().filter(x ->
                    filter.filter(x.getPoint())
            ).map(x -> setFlightInfo(x)).collect(Collectors.toList());
        }
        return results;
    }

    public AirplaneEntityVo setFlightInfo(AirplaneEntityVo entity) {
        HkAdsbIdent fl = null;
        String[] types = {"AIRBUSA321-271N NEO", "BOING737"};
        String type = types[RandomUtil.randomInt(0, 2)];
        entity.setFlightICAO(String.format("CAU%s", entity.getAirplaneICAO().substring(2, 6)));
        entity.setType(type);
//        try{
////            fl = flightDao.findByIcaoAndTime(entity.getAirplaneICAO(), entity.getTime());
//            entity.setFlightICAO(fl.getCallsign());
//            entity.setType(fl.getPlaneType());
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.error(String.format("Failed to get flight info for %s!", entity.getAirplaneICAO()));
//        }
        return entity;
    }

    @Override
    public List<AirplaneEntityVo> findPlaneByCircle(double slon, double slat, float radius) {
        long ts = DateUtils.getNowDate().getTime();
        return findHisPlaneByCircle(slon, slat, radius, ts);
    }

    @Override
    public List<AirplaneEntityVo> findHisPlaneByCircle(double slon, double slat, float radiusKm, long time) {
        // {minLat, minLng, maxLat, maxLng}
        double[] range = HkjsUtils.getAround(slat, slon, radiusKm);
        return findHisPlaneByArea(range[1], range[0], range[3], range[2], time, new CirclePointFilter(slat, slon, radiusKm));
    }

    @Override
    public void createTables() {
        if (!HBaseService.existTable(connection, HkjsUtils.HK_PLANE_TABLE))
            HBaseService.createTable(connection, HkjsUtils.HK_PLANE_TABLE, new String[]{HkjsUtils.HK_PLANE_CF_VECTORS});
        if (!HBaseService.existTable(connection, HkjsUtils.HK_GEOHASH_TABLE))
            HBaseService.createTable(connection, HkjsUtils.HK_GEOHASH_TABLE, new String[]{HkjsUtils.HK_GEOHASH_CF_VECTORS});
        if (!HBaseService.existTable(connection, HkjsUtils.HK_FLIGHT_TABLE))
            HBaseService.createTable(connection, HkjsUtils.HK_FLIGHT_TABLE, new String[]{HkjsUtils.HK_FLIGHT_CF_INFO});
    }

    /**
     * @param startTimeMilli
     * @param cntTrack       轨迹点数
     * @param cntPlane       飞机数量
     * @param icaoPrefix:    FF
     * @return
     */
    @Override
    public void initPlaneByCity(long startTimeMilli, int cntTrack, int cntPlane, String icaoPrefix, double radius) {
        byte dicao2 = 0x00;
        String flightPre = "CAU";
        for (ConfigValues configValues : DBConfigUtils.getAllCitys()) {
            Point point = DBConfigUtils.getConfigValue(configValues);
            String icaoPrel2 = icaoPrefix + ADSBTool.toHexString(dicao2);
            String flightPrel = flightPre + ADSBTool.toHexString(dicao2);
            dicao2++;
            byte dicao3 = 0x00;
            for (int i = 0; i < cntPlane; i++) {
                int dir = RandomUtil.randomInt(0, 360);
                double tgle = Math.toRadians(dir);
                double datLat = Math.cos(tgle) * radius;
                double datLon = Math.sin(tgle) * radius;
                putPlane(startTimeMilli / 1000, point.getLatitude() + i * datLat,
                        point.getLongitude() + i * datLon, i, dir,
                        icaoPrel2 + ADSBTool.toHexString(dicao3),
                        flightPrel + ADSBTool.toHexString(dicao3),
                        cntTrack, radius);
                dicao3++;
            }
        }
    }

    @Override
    public void initPlaneByAirport(long startTimeMilli, int cntTrack, int cntPlane, String icaoPrefix, double radius) {
        List<HkAirport> airportList = hkAirportService.selectHkAirportList(null);
        byte dicao2 = 0x00;
        String flightPre = "CAU";
        for (HkAirport ap : airportList) {
            if (!":Asia/Shanghai".equals(ap.getTz())) {
                continue;
            }
            String icaoPrel2 = icaoPrefix + ADSBTool.toHexString(dicao2);
            String flightPrel = flightPre + ADSBTool.toHexString(dicao2);
            dicao2++;
            byte dicao3 = 0x00;
            for (int i = 0; i < cntPlane; i++) {
                int dir = RandomUtil.randomInt(0, 360);
                double tgle = Math.toRadians(dir);
                double datLat = Math.cos(tgle) * radius;
                double datLon = Math.sin(tgle) * radius;
                putPlane(startTimeMilli / 1000, ap.getLatitude() + i * datLat,
                        ap.getLongitude() + i * datLon, i, dir,
                        icaoPrel2 + ADSBTool.toHexString(dicao3),
                        flightPrel + ADSBTool.toHexString(dicao3),
                        cntTrack, radius);
                dicao3++;
            }
        }
    }

    @Override
    public boolean initPlaneByFile(long startTimeMilli, int cntTrack, int cntPlane, String icaoPrefix, String filePath, double radius) {
        if (StringUtils.isEmpty(filePath)) {
            filePath = "/tmp/cityLocation.txt";
        }
        if (StringUtils.isEmpty(icaoPrefix)) {
            icaoPrefix = "FE";
        }
        byte dicao2 = 0x00;
        String flightPre = "CAU";
        boolean ok = true;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line = null;
            String[] arr = null;

            while ((line = br.readLine()) != null && ok) {
                String icaoPrel2 = icaoPrefix + ADSBTool.toHexString(dicao2);
                String flightPrel = flightPre + ADSBTool.toHexString(dicao2);
                dicao2++;
                arr = line.split(",");
                byte dicao3 = 0x00;
                for (int i = 0; i < cntPlane; i++) {
                    int dir = RandomUtil.randomInt(0, 360);
                    double tgle = Math.toRadians(dir);
                    double datLat = Math.cos(tgle) * radius;
                    double datLon = Math.sin(tgle) * radius;

                    putPlane(startTimeMilli / 1000, Double.parseDouble(arr[2]) + i * datLat,
                            Double.parseDouble(arr[1]) + i * datLon, i, dir,
                            icaoPrel2 + ADSBTool.toHexString(dicao3),
                            flightPrel + ADSBTool.toHexString(dicao3),
                            cntTrack, radius);
                    dicao3++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ok = false;
        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }

    private void putPlane(long stimeSec, double lat, double lon, int ind, int dir, String icao, String flight, int loop, double radius) {
        float alt = 800 + 50 * ind;
        float speed = 500 + 10 * ind;
        String[] types = {"AIRBUSA321-271N NEO", "BOING737"};
        String type = types[ind % 2];
//        HkAdsbPostion obj = new HkAdsbPostion(lat, lon, alt, false);


        double tgle = Math.toRadians(dir);
        double datLat = Math.cos(tgle) * radius;
        double datLon = Math.sin(tgle) * radius;


        HkAdsbIdent ident = new HkAdsbIdent();
        ident.setCallsign(flight);
        ident.setIcao(icao);
        ident.setTimestamp(stimeSec * 1000);
        if (type != null)
            ident.setPlaneType(type);
//        flightDao.putFlight(ident);
        decoder.saveToBuffer(ident);
//        List<Put> puts = new ArrayList<>();
//        List<Put> puts2 = new ArrayList<>();
        for (int timeOffset = 0; timeOffset < loop; timeOffset += 2) {
            long time = (stimeSec + timeOffset) * 1000;
            HkAdsbPostion obj = new HkAdsbPostion();
            obj.setIcao(icao);
            obj.setTimestamp(time);
            obj.setLatitude(lat + datLat * timeOffset);
            obj.setLongitude(lon + datLon * timeOffset);
//            puts.add(geohashDao.convertPosition(obj));
//            puts2.add(planeDao.convertPosition(obj));
            decoder.saveToBuffer(obj);
            HkAdsbSpeed spd = new HkAdsbSpeed();
            spd.setTrack(dir);
            spd.setGspeed(speed);
            spd.setIcao(icao);
            spd.setTimestamp(time);
            spd.setLon(obj.getLongitude());
            spd.setLat(obj.getLatitude());
//            puts.add(geohashDao.convertSpeed(spd));
//            puts2.add(planeDao.convertSpeed(spd));
            decoder.saveToBuffer(spd);
        }
//        geohashDao.putPuts(puts);
//        planeDao.putList(puts2);
    }

}
