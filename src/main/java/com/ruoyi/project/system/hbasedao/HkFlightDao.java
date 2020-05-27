package com.ruoyi.project.system.hbasedao;

import com.ruoyi.common.utils.HkjsUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.domain.HkAdsbIdent;
import com.ruoyi.project.system.domain.HkAdsbSpeed;
import com.ruoyi.project.system.service.impl.HBaseService;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 5/9/20.
 */
@Component
public class HkFlightDao {

    private static final long CURRENT_TIME_OFFSET = 60000;
    @Autowired
    private Connection connection;

    private byte[] getRowKey(String callsign, long time, String icao) {
        return Bytes.add(getRowKey(callsign, time),
                Bytes.toBytes(icao.toLowerCase()));
    }

    private byte[] getRowKey(String callsign, long time) {
        while (callsign.length() < 8) {
            callsign += " ";
        }
        return Bytes.add(Bytes.toBytes(callsign.toUpperCase()),
                Bytes.toBytes(Long.MAX_VALUE - time));
    }

    private byte[] getRowKey(String callsign) {
        return Bytes.toBytes(callsign.toUpperCase());
    }

/*    private byte[] getRowKey(String callsign, boolean start) {
        String icao = start ? "000000" : "ffffff";
        while(callsign.length() < 8){
            callsign += " ";
        }
        if(start){
            return callsign.toUpperCase().getBytes();
        }
        return getRowKey(callsign, icao);
    }*/

    public boolean putFlight(HkAdsbIdent hkAdsbIdent) {
        return HBaseService.putRow(connection, HkjsUtils.HK_FLIGHT_TABLE, convertIdent(hkAdsbIdent));
    }

    public boolean putList(List<Put> puts) {
        return HBaseService.putRows(connection, HkjsUtils.HK_FLIGHT_TABLE, puts);
    }

    public Put convertIdent(HkAdsbIdent hkAdsbIdent) {
        String callsign = hkAdsbIdent.getCallsign();
        boolean failed = false;
        byte[] start = getRowKey(callsign);
        byte[] end = getRowKey(callsign, Long.MIN_VALUE, "ffffff");
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_FLIGHT_TABLE, start, end);
        Result result = null;
        try {
            result = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
            failed = true;
        }
        if(scanner != null){
            scanner.close();
        }
        if(failed)
            return null;
        byte[] rkey = null;
        if (result != null)
            rkey = result.getRow();
        if(rkey == null)
            rkey = getRowKey(hkAdsbIdent.getCallsign(), hkAdsbIdent.getTimestamp(), hkAdsbIdent.getIcao());
        Put put = new Put(rkey);
        if (StringUtils.isNotEmpty(hkAdsbIdent.getPlaneType())) {
            put.addColumn(HkjsUtils.HK_FLIGHT_CF_INFO_BYTES, Bytes.toBytes(hkAdsbIdent.getPlaneType()), Bytes.toBytes(hkAdsbIdent.getTimestamp()));
        } else {
            put.addColumn(HkjsUtils.HK_FLIGHT_CF_INFO_BYTES, Bytes.toBytes("UNKNOWN"), Bytes.toBytes(hkAdsbIdent.getTimestamp()));
        }
        return put;
    }

    /**
     * 根据航班号查询最近时间的记录, 注意rowkey的时间是经过反转的
     * 所以rowkey只传入callsign且只查询一行，那查到的就是该航班最近的记录
     *
     * @param callsign
     * @return
     */
    public HkAdsbIdent getLastestByFlight(String callsign) throws IOException {
        byte[] start = getRowKey(callsign);
        byte[] end = getRowKey(callsign, Long.MIN_VALUE, "ffffff");
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_FLIGHT_TABLE, start, end);
        Result result = scanner.next();
        HkAdsbIdent ident = null;
        if (result != null)
            ident = rowToHkAdsbIdent(result, System.currentTimeMillis() - CURRENT_TIME_OFFSET);
        if (scanner != null) {
            scanner.close();
        }
        return ident;
    }

    /**
     * 根据航班号查询历史时间的记录，注意rowkey的时间是经过反转的
     * 所以startKey之后的数据库行的时间，其实是比time更早的,只查询一行就是比time更早且里time最近的
     *
     * @param callsign
     * @param time
     * @return
     */
    public HkAdsbIdent getHisByFlight(String callsign, long time) throws IOException {
        byte[] start = getRowKey(callsign, time + 1);
        byte[] end = getRowKey(callsign, Long.MIN_VALUE, "ffffff");
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_FLIGHT_TABLE, start, end);
        Result result = scanner.next();
        HkAdsbIdent ident = null;
        if (result != null)
            ident = rowToHkAdsbIdent(result, time);
        if (scanner != null) {
            scanner.close();
        }
        return ident;
    }

    /**
     * 根据飞机ICAO地址查询该飞机最近的记录
     * 通过行键过滤器查得该飞机所有历史记录，因为数据是按照时间倒序排列的，所以第一条就是最近时间的记录
     *
     * @param icao
     * @return
     * @throws IOException
     */
    public HkAdsbIdent getLastestByIcao(String icao) throws IOException {
        byte[] skey = getRowKey(" ", Long.MAX_VALUE, icao);
        byte[] ekey = getRowKey("zzzzzzzz", Long.MIN_VALUE, icao);
        FilterList fl = new FilterList((FilterList.Operator.MUST_PASS_ALL));
        fl.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(String.format(".*%s$", icao))));
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_FLIGHT_TABLE, skey, ekey, 3000, fl);
        Result item = scanner.next(); // the first row is the lastest
        HkAdsbIdent ident = null;
        if (item != null) {
            ident = rowToHkAdsbIdent(item, System.currentTimeMillis() - CURRENT_TIME_OFFSET);
        }
        if (scanner != null) {
            scanner.close();
        }
        return ident;
    }

    /**
     * 查询与给定时间time最近的航班号和飞机型号
     *
     * @param icaoIn
     * @param time
     * @return
     * @throws IOException
     */
    public HkAdsbIdent getHisByIcao(String icaoIn, long time) throws IOException {
        byte[] skey = getRowKey(" ", time + 1, icaoIn);
        byte[] ekey = getRowKey("zzzzzzzz", Long.MIN_VALUE, icaoIn);
        FilterList fl = new FilterList((FilterList.Operator.MUST_PASS_ALL));
        fl.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(String.format(".*%s$", icaoIn))));
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_FLIGHT_TABLE, skey, ekey, 3000, fl);
        Result item = null;
        HkAdsbIdent ident = null;
        long timeoffset = Long.MAX_VALUE;
        while ((item = scanner.next()) != null) {
            HkAdsbIdent tmp = rowToHkAdsbIdent(item, time);
            long newOffset = tmp.getTimestamp() - time;
            if (newOffset >= 0 && newOffset < timeoffset) {
                ident = tmp;
            }
        }

        if (scanner != null) {
            scanner.close();
        }
        return ident;
    }

    /**
     * 查询该航班的所有记录
     *
     * @param callsign
     * @return
     * @throws IOException
     */
    public List<HkAdsbIdent> findByFlight(String callsign) throws IOException {
        byte[] start = getRowKey(callsign);
        byte[] end = getRowKey(callsign, Long.MIN_VALUE, "ffffff");
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_FLIGHT_TABLE, start, end);
        return findByScanner(scanner);
    }

    /**
     * 根据icao查询该飞机所有记录
     *
     * @param icao
     * @return
     * @throws IOException
     */
    public List<HkAdsbIdent> findByIcao(String icao) throws IOException {
        byte[] skey = getRowKey(" ", Long.MAX_VALUE, icao);
        byte[] ekey = getRowKey("zzzzzzzz", Long.MIN_VALUE, icao);
        FilterList fl = new FilterList((FilterList.Operator.MUST_PASS_ALL));
        fl.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(String.format(".*%s$", icao))));
        ResultScanner scanner = HBaseService.scan(connection, HkjsUtils.HK_FLIGHT_TABLE, skey, ekey, 3000, fl);
        return findByScanner(scanner);
    }


    public List<HkAdsbIdent> findByScanner(ResultScanner scanner, long time) throws IOException {
        List<HkAdsbIdent> results = new ArrayList<>();
        Result item = null;
        while ((item = scanner.next()) != null) {
            results.add(rowToHkAdsbIdent(item, time));
        }
        if (scanner != null) {
            scanner.close();
        }
        return results;
    }

    public List<HkAdsbIdent> findByScanner(ResultScanner scanner) throws IOException {
        List<HkAdsbIdent> results = new ArrayList<>();
        Result item = null;
        while ((item = scanner.next()) != null) {
            byte[] rowkey = item.getRow();
            String csn = Bytes.toString(rowkey, 0, rowkey.length - 6);
            String icao = Bytes.toString(rowkey, rowkey.length - 6, 6);
            Map<byte[], byte[]> cfMap = item.getFamilyMap(HkjsUtils.HK_FLIGHT_CF_INFO_BYTES);
            for (Map.Entry<byte[], byte[]> entry : cfMap.entrySet()) {
                //列名为飞机型号
                String planeType = Bytes.toString(entry.getKey());
                HkAdsbIdent ident = new HkAdsbIdent();
                ident.setCallsign(csn);
                ident.setIcao(icao);
                long timeofType = Bytes.toLong(entry.getValue());
                ident.setTimestamp(timeofType);
                ident.setPlaneType(planeType);
                results.add(ident);
            }
        }
        if (scanner != null) {
            scanner.close();
        }
        return results;
    }

    public HkAdsbIdent rowToHkAdsbIdent(Result item, long time) {
        byte[] rowkey = item.getRow();
        String csn = Bytes.toString(rowkey, 0, 8);
        String icao = Bytes.toString(rowkey, rowkey.length - 6, 6);
        HkAdsbIdent ident = new HkAdsbIdent();
        ident.setCallsign(csn);
        ident.setIcao(icao);
        Map<byte[], byte[]> cfMap = item.getFamilyMap(HkjsUtils.HK_FLIGHT_CF_INFO_BYTES);
        long timeoffset = Long.MIN_VALUE;

        for (Map.Entry<byte[], byte[]> entry : cfMap.entrySet()) {
            //列名为飞机型号
            String planeType = Bytes.toString(entry.getKey());
            long timeofType = Bytes.toLong(entry.getValue());
            long newOffset = timeofType - time;
            if (timeoffset < 0 && newOffset > timeoffset) {
                timeoffset = newOffset;
                ident.setTimestamp(timeofType);
                ident.setPlaneType(planeType);
            } else if (timeoffset > 0 && newOffset < timeoffset && newOffset >= 0) {
                timeoffset = newOffset;
                ident.setTimestamp(timeofType);
                ident.setPlaneType(planeType);
            }
        }
        return ident;
    }

    /*public boolean delete(String callsign, String icao){
        return HBaseService.delete(connection, HkjsUtils.HK_FLIGHT_TABLE, getRowKey(callsign, icao));
    }*/
}
