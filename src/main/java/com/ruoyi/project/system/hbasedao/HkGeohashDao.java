package com.ruoyi.project.system.hbasedao;

import com.ruoyi.common.utils.HkjsUtils;
import com.ruoyi.project.system.domain.HkAdsbPostion;
import com.ruoyi.project.system.domain.HkAdsbSpeed;
import com.ruoyi.project.system.domain.vo.AirplaneEntityVo;
import com.ruoyi.project.system.domain.vo.Point;
import com.ruoyi.project.system.service.impl.HBaseService;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 5/9/20.
 */
@Component
public class HkGeohashDao {
    @Autowired
    private Connection connection;

    private byte[] getRowKey(long time, String geohash) {
        time = time / 5000 * 5000; // round for 5 seconds
        return Bytes.add(
                geohash.substring(0, HkjsUtils.HK_GEOHASH_PRE_LEN).getBytes(),
                Bytes.toBytes(HkjsUtils.timestampToDate(time)),
                geohash.substring(HkjsUtils.HK_GEOHASH_PRE_LEN).getBytes());
    }

    private byte[] getRowKey(long time, String geohash, String icao) {
        // rowkey = [1byte_hash+time+8byte_hash+icao]
        return Bytes.add(getRowKey(time, geohash), Bytes.fromHex(icao));
    }

    private byte[] getRowKey(HkAdsbPostion hkAdsbPostion) {
        String geohash = HkjsUtils.geohash(hkAdsbPostion.getLatitude(), hkAdsbPostion.getLongitude());
        return getRowKey(hkAdsbPostion.getTimestamp(), geohash, hkAdsbPostion.getIcao());
    }

    private byte[] getRowKey(HkAdsbSpeed spd) {
        String geohash = HkjsUtils.geohash(spd.getLat(), spd.getLon());
        return getRowKey(spd.getTimestamp(), geohash, spd.getIcao());
    }

    public boolean putHashPosition(HkAdsbPostion hkAdsbPostion) {
        return HBaseService.putRow(connection, HkjsUtils.HK_GEOHASH_TABLE, convertPosition(hkAdsbPostion));
    }

    public boolean putPuts(List<Put> puts){
        return HBaseService.putRows(connection, HkjsUtils.HK_GEOHASH_TABLE, puts);
    }

    public Put convertPosition(HkAdsbPostion hkAdsbPostion){
        int timeOffset = (int) (hkAdsbPostion.getTimestamp() % 5000);
        Put put = new Put(getRowKey(hkAdsbPostion));
        put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_LAT_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getLatitude()));
        put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_LON_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getLongitude()));
        put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_ALT_G_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getAltitude()));
        put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_TOFFSET_QUALIFIER_BYTES, Bytes.toBytes(timeOffset));
        if (hkAdsbPostion.getTrack() >= 0) {
            put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.getTrack()));
        }
        put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_SURFACE_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbPostion.isSurface()));
        return put;
    }

    public Put convertSpeed(HkAdsbSpeed hkAdsbSpeed){
        Put put = new Put(getRowKey(hkAdsbSpeed));
        put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getTrack()));
        if(hkAdsbSpeed.getSpeed() >= 0) {
            put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_SPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getSpeed()));
        }
        if (hkAdsbSpeed.getGspeed() >= 0) {
            put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getGspeed()));
        }
        if (hkAdsbSpeed.getVspeed() != 0) {
            put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_VSPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getVspeed()));
        }
        return put;
    }

/*    public Put convertSpeed(HkAdsbSpeed hkAdsbSpeed, HkAdsbPostion pos){
        Put put = new Put(getRowKey(pos));
        put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getTrack()));
        if(hkAdsbSpeed.getSpeed() >= 0) {
            put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_SPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getSpeed()));
        }
        if (hkAdsbSpeed.getGspeed() >= 0) {
            put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getGspeed()));
        }
        if (hkAdsbSpeed.getVspeed() != 0) {
            put.addColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_VSPEED_QUALIFIER_BYTES, Bytes.toBytes(hkAdsbSpeed.getVspeed()));
        }
        return put;
    }*/

    public boolean putHashSpeed(HkAdsbSpeed hkAdsbSpeed){
        if(hkAdsbSpeed.getLon() == null || hkAdsbSpeed.getLat() == null) {
            return false;
        }
        return HBaseService.putRow(connection, HkjsUtils.HK_GEOHASH_TABLE, convertSpeed(hkAdsbSpeed));
    }

    private AirplaneEntityVo rowToEntity(Result item) {
        AirplaneEntityVo.AirplaneEntityVoBuilder builder = AirplaneEntityVo.builder();
        Point.PointBuilder pb = Point.builder();
        byte[] rkey = item.getRow();
        builder.airplaneICAO(Bytes.toHex(rkey, rkey.length - 3, 3));
        long millisec = HkjsUtils.dateTimeToMilli(Bytes.toString(rkey, 1, 14));
        builder.time(millisec + Bytes.toInt(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_TOFFSET_QUALIFIER_BYTES)));
        pb.latitude(Bytes.toDouble(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_LAT_QUALIFIER_BYTES)));
        pb.longitude(Bytes.toDouble(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_LON_QUALIFIER_BYTES)));
        builder.point(pb.build());
        builder.height(Bytes.toFloat(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_ALT_G_QUALIFIER_BYTES)));
        builder.isSurface(Bytes.toBoolean(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_SURFACE_QUALIFIER_BYTES)));
        if (item.containsColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES)) {
            builder.direction(Bytes.toFloat(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_TRACK_QUALIFIER_BYTES)));
        }
        if (item.containsColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES)) {
            builder.speed(Bytes.toFloat(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_GSPEED_QUALIFIER_BYTES)));
        } else if(item.containsColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_SPEED_QUALIFIER_BYTES)) {
            builder.speed(Bytes.toFloat(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_SPEED_QUALIFIER_BYTES)));
        }
        if (item.containsColumn(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_VSPEED_QUALIFIER_BYTES)) {
            builder.vspeed(Bytes.toFloat(item.getValue(HkjsUtils.HK_GEOHASH_CF_VECTORS_BYTES, HkjsUtils.HK_VSPEED_QUALIFIER_BYTES)));
        }
        return builder.build();
    }


    public Map<String, AirplaneEntityVo> findByArea(long time, String startHash, String endHash) throws IOException {
        byte[] start = getRowKey(time, startHash);
        byte[] tail = getRowKey(time, endHash);
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_GEOHASH_TABLE, start, tail);
        return findByScanner(scanner);
    }

    public Map<String, AirplaneEntityVo> findByTimeFilter(long time, String startHash, String endHash) throws IOException {
        byte[] start = getRowKey(time, startHash);
        byte[] tail = getRowKey(time, endHash);
        FilterList fl = new FilterList((FilterList.Operator.MUST_PASS_ALL));
        fl.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(HkjsUtils.timestampToDate(time / 5000 * 5000))));
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_GEOHASH_TABLE, start, tail, 3000, fl);
        return findByScanner(scanner);
    }

    public Map<String, AirplaneEntityVo> findByScanner(ResultScanner scanner) throws IOException {
        Map<String, AirplaneEntityVo> results = new HashMap<>();
        Result item = null;
        String icao;
        while ((item = scanner.next()) != null) {
            AirplaneEntityVo entity = rowToEntity(item);
            if (entity != null) {
                icao = entity.getAirplaneICAO();
                if (results.get(icao) == null) {
                    results.put(icao, entity);
                } else if (results.get(icao).getTime() < entity.getTime()) {
                    results.put(icao, entity);
                }
            }
        }
        if (scanner != null) {
            scanner.close();
        }
        return results;
    }
}
