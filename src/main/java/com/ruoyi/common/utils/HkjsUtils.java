package com.ruoyi.common.utils;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.Math.*;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;

/**
 * Created by root on 4/26/20.
 */
public class HkjsUtils {

    public static final String HK_ADSB_QUEUE = "adsbqueue";
    public static final String HK_PLANE_TABLE = "hkjsplane";
    public static final String HK_PLANE_CF_VECTORS = "v";
    public static final byte[] HK_PLANE_CF_VECTORS_BYTES = HK_PLANE_CF_VECTORS.getBytes();
    public static final String HK_LAT_QUALIFIER = "w";
    public static final byte[] HK_LAT_QUALIFIER_BYTES = HK_LAT_QUALIFIER.getBytes();
    public static final String HK_LON_QUALIFIER = "j";
    public static final byte[] HK_LON_QUALIFIER_BYTES = HK_LON_QUALIFIER.getBytes();
    public static final String HK_ALT_B_QUALIFIER = "b";
    public static final byte[] HK_ALT_B_QUALIFIER_BYTES = HK_ALT_B_QUALIFIER.getBytes();
    public static final String HK_ALT_G_QUALIFIER = "g";
    public static final byte[] HK_ALT_G_QUALIFIER_BYTES = HK_ALT_G_QUALIFIER.getBytes();
    public static final String HK_TRACK_QUALIFIER = "t";
    public static final byte[] HK_TRACK_QUALIFIER_BYTES = HK_TRACK_QUALIFIER.getBytes();  // 航向角度
    public static final String HK_GSPEED_QUALIFIER = "gs";
    public static final byte[] HK_GSPEED_QUALIFIER_BYTES = HK_GSPEED_QUALIFIER.getBytes(); // 地速
    public static final String HK_SPEED_QUALIFIER = "s";
    public static final byte[] HK_SPEED_QUALIFIER_BYTES = HK_SPEED_QUALIFIER.getBytes();  // 空速
    public static final String HK_VSPEED_QUALIFIER = "v";
    public static final byte[] HK_VSPEED_QUALIFIER_BYTES = HK_VSPEED_QUALIFIER.getBytes();
    public static final String HK_SURFACE_QUALIFIER = "u";
    public static final byte[] HK_SURFACE_QUALIFIER_BYTES = HK_SURFACE_QUALIFIER.getBytes();
    public static final String HK_TOFFSET_QUALIFIER = "to";
    public static final byte[] HK_TOFFSET_QUALIFIER_BYTES = HK_TOFFSET_QUALIFIER.getBytes();

    public static final String HK_GEOHASH_TABLE = "hkjsgeohash";
    public static final int HK_GEOHASH_PRE_LEN = 1;
    public static final String HK_GEOHASH_CF_VECTORS = "v";
    public static final byte[] HK_GEOHASH_CF_VECTORS_BYTES = HK_GEOHASH_CF_VECTORS.getBytes();


    public static final String HK_FLIGHT_TABLE = "hkjsflight";
    public static final String HK_FLIGHT_CF_INFO = "c";
    public static final byte[] HK_FLIGHT_CF_INFO_BYTES = HK_FLIGHT_CF_INFO.getBytes();
    public static final String HK_TIME_QUALIFIER = "ts";
    public static final String HK_PLANE_TYPE_QUALIFIER = "pt";
    public static final byte[] HK_TIME_QUALIFIER_BYTES = HK_TIME_QUALIFIER.getBytes();
    public static final byte[] HK_PLANE_TYPE_QUALIFIER_BYTES = HK_PLANE_TYPE_QUALIFIER.getBytes();

    public static final String geohash(double lat, double lon){
        GeoHash hash = GeoHash.withCharacterPrecision(lat, lon, 8);
        return hash.toBase32();
    }

    public static List<String> splitArea(double slon, double slat, double elon, double elat){
        //        String[] pres = {"0", "1", "2", "3", "4", "5","6","7","8","9","b","c","d","e","f","g","h","j","k","m","n","p","q",
//                "r","s","t","u","v","w","x","y","z"};
        List<String> results = new ArrayList<>();
        String hash1 = geohash(slat, slon);
        String hash2 = geohash(elat, elon);
        String pre1 = hash1.substring(0,1);
        String pre2 = hash2.substring(0,1);
        if(pre1.equals(pre2)){
            results.add(hash1);
            results.add(hash2);
            return results;
        }
        GeoHash geoHash1 = GeoHash.fromGeohashString(pre1);
        GeoHash geoHash2 = GeoHash.fromGeohashString(pre2);
        BoundingBox boundingBox1 = geoHash1.getBoundingBox();
        BoundingBox boundingBox2 = geoHash2.getBoundingBox();

        if(geoHash1.getNorthernNeighbour().toBase32().equals(pre2)){
            // 南北相邻
            String resHash1 = HkjsUtils.geohash(boundingBox1.getNorthLatitude(), elon);
            String resHash2 = HkjsUtils.geohash(boundingBox2.getSouthLatitude(), slon);
            results.add(hash1);
            results.add(resHash1);
            results.add(resHash2);
            results.add(hash2);
        }else if (geoHash1.getEasternNeighbour().toBase32().equals(pre2)){
            // 东西相邻
            String resHash1 = HkjsUtils.geohash(elat, boundingBox1.getEastLongitude());
            String resHash2 = HkjsUtils.geohash(slat, boundingBox2.getWestLongitude());
            results.add(hash1);
            results.add(resHash1);
            results.add(resHash2);
            results.add(hash2);
        }else if(boundingBox1.getNorthLatitude() == boundingBox2.getSouthLatitude() && boundingBox1.getEastLongitude() == boundingBox2.getWestLongitude()){
            // 对角
            String resHash1 = HkjsUtils.geohash(boundingBox1.getNorthLatitude(), boundingBox1.getEastLongitude());
            String resHash2 = HkjsUtils.geohash(slat, boundingBox2.getWestLongitude());
            String resHash3 = HkjsUtils.geohash(boundingBox1.getNorthLatitude(), elon);
            String resHash4 = HkjsUtils.geohash(boundingBox1.getNorthLatitude(), slon);
            String resHash5 = HkjsUtils.geohash(elat, boundingBox1.getEastLongitude());
            String resHash6 = HkjsUtils.geohash(boundingBox2.getSouthLatitude(), boundingBox2.getWestLongitude());
            results.add(hash1);
            results.add(resHash1);
            results.add(resHash2);
            results.add(resHash3);
            results.add(resHash4);
            results.add(resHash5);
            results.add(resHash6);
            results.add(hash2);
        }else{
            // 更大的查询区域，跨越多个大区  not supported yet
            return null;
        }
        return results;
    }

    public static List<String> splitArea(String hash1, String hash2){
        Double slat = GeoHash.fromGeohashString(hash1).getBoundingBox().getSouthLatitude();
        Double slon = GeoHash.fromGeohashString(hash1).getBoundingBox().getWestLongitude();
        Double elat = GeoHash.fromGeohashString(hash2).getBoundingBox().getNorthLatitude();
        Double elon = GeoHash.fromGeohashString(hash2).getBoundingBox().getEastLongitude();

        return splitArea(slon, slat, elon, elat);
    }

    public static final byte[] concatBytes(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String timestampToDate(long milliseconds){
        return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date(milliseconds));

    }

    public static long dateTimeToMilli(String datetime){
        LocalDateTime t = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return t.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * 计算两点之间的距离
     * @param long1
     * @param lat1
     * @param long2
     * @param lat2
     * @return 两点之间的距离,单位:米
     */
    public static double distanceByLongNLat(double long1, double lat1, double long2, double lat2) {
        double lon0r = toRadians(long1);
        double lat0r = toRadians(lat1);
        double lon1r = toRadians(long2);
        double lat1r = toRadians(lat2);
        double a = pow(sin((lat1r - lat0r) / 2.0), 2);
        double b = cos(lat0r) * cos(lat1r) * pow(sin((lon1r - lon0r) / 2.0), 2);

        return 6371000.0 * 2 * asin(sqrt(a + b));
    }

    public static double[] getAround(double lat, double lon, float raidusKm) {

        Double latitude = lat;
        Double longitude = lon;
        //地球周长=24901英里，1英里=1609米
        Double degree = (24901 * 1609) / 360.0;
        double raidusMeter = raidusKm*1000;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMeter;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMeter;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[]{minLat, minLng, maxLat, maxLng};
    }

}
