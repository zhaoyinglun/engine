package com.ruoyi.project.system.hbasedao;

import com.ruoyi.common.utils.ADSBTool;
import com.ruoyi.common.utils.HkjsUtils;
import com.ruoyi.project.system.domain.HkAdsbBase;
import com.ruoyi.project.system.domain.HkAdsbPostion;
import com.ruoyi.project.system.domain.HkAdsbSpeed;
import com.ruoyi.project.system.service.impl.HBaseService;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 5/9/20.
 */
@Component
public class HkPlaneDao {

    @Autowired
    private Connection connection;

    private byte[] getRowKey(HkAdsbBase hkAdsbBase) {
        return Bytes.add(Bytes.fromHex(hkAdsbBase.getIcao()), Bytes.toBytes(hkAdsbBase.getTimestamp()));
    }

    private byte[] getRowKey(String icao, long ts){
        return Bytes.add(Bytes.fromHex(icao), Bytes.toBytes(ts));
    }

    public boolean putPostion(HkAdsbPostion hkAdsbPostion) {
        return HBaseService.putRow(connection, HkjsUtils.HK_PLANE_TABLE, convertPosition(hkAdsbPostion));
    }

    public Put convertPosition(HkAdsbPostion hkAdsbPostion){
        Put put = new Put(getRowKey(hkAdsbPostion));
        put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_LAT_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getLatitude()));
        put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_LON_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getLongitude()));
        put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_ALT_G_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getAltitude()));
        if (hkAdsbPostion.getTrack() >= 0) {
            put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getTrack()));
        }
        if (hkAdsbPostion.getGspeed() >= 0) {
            put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getGspeed()));
        }
        put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_SURFACE_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.isSurface()));
        return put;
    }

    public Put convertSpeed(HkAdsbSpeed hkAdsbSpeed){
        Put put = new Put(getRowKey(hkAdsbSpeed));
        put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getTrack()));
        if(hkAdsbSpeed.getSpeed() >= 0) {
            put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_SPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getSpeed()));
        }
        if (hkAdsbSpeed.getGspeed() >= 0) {
            put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getGspeed()));
        }
        if (hkAdsbSpeed.getVspeed() != 0) {
            put.addColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_VSPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getVspeed()));
        }
        return put;
    }

    public boolean putSpeed(HkAdsbSpeed hkAdsbSpeed) {
        return HBaseService.putRow(connection, HkjsUtils.HK_PLANE_TABLE, convertSpeed(hkAdsbSpeed));
    }

    public boolean putList(List<Put> puts){
        return HBaseService.putRows(connection, HkjsUtils.HK_PLANE_TABLE, puts);
    }

    public List<HkAdsbPostion> findBetweenTime(String icao, long stime, long etime) throws IOException {
        List<HkAdsbPostion> results = new ArrayList<>();
        byte[] start = getRowKey(icao, stime);
        byte[] tail = getRowKey(icao, etime);
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_PLANE_TABLE, start, tail);
        Result item = null;
        while((item = scanner.next()) != null){
            HkAdsbPostion p = rowToHkAdsbPostion(item);
            if(p != null){
                results.add(p);
            }
        }
        if(scanner != null){
            scanner.close();
        }
        return results;
    }

    private HkAdsbPostion rowToHkAdsbPostion(Result item){
        HkAdsbPostion pos = new HkAdsbPostion();
        byte[] row = item.getRow();
        String icaoAddr = Bytes.toHex(row, 0, 3);
        long time = Bytes.toLong(row, 3, row.length-3);
        if(item.containsColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_LAT_QUALIFIER_BYTES)){
            pos.setTimestamp(time);
            pos.setIcao(icaoAddr);
            pos.setLatitude(Bytes.toDouble(item.getValue(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_LAT_QUALIFIER_BYTES)));
            pos.setLongitude(Bytes.toDouble(item.getValue(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_LON_QUALIFIER_BYTES)));
            pos.setAltitude(Bytes.toFloat(item.getValue(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_ALT_G_QUALIFIER_BYTES)));
            pos.setSurface(Bytes.toBoolean(item.getValue(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_SURFACE_QUALIFIER_BYTES)));
            if(item.containsColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES)){
                pos.setTrack(Bytes.toFloat(item.getValue(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES)));
            }
            if(item.containsColumn(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES)){
                pos.setGspeed(Bytes.toFloat(item.getValue(HkjsUtils.HK_PLANE_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES)));
            }
            return pos;
        }
        return null;
    }
    public List<HkAdsbPostion> findByIcao(String icao) throws IOException {
        List<HkAdsbPostion> results = new ArrayList<>();
        byte[] start = Bytes.fromHex(icao);
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_PLANE_TABLE, start);
        Result item = null;
        while((item = scanner.next()) != null){
            HkAdsbPostion p = rowToHkAdsbPostion(item);
            if(p != null){
                results.add(p);
            }
        }
        if(scanner != null){
            scanner.close();
        }
        return results;
    }
}
