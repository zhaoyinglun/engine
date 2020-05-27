package com.ruoyi.common.utils;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import com.ruoyi.project.system.domain.vo.Point;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Created by root on 5/9/20.
 */
public class HkjsUtilsTest {
    @Test
    public void testGeohash(){
        String hash = HkjsUtils.geohash(39.916527, 116.397128);
        System.out.println(hash); //wx4g0dtf
        System.out.println(HkjsUtils.geohash(1.0, 10.0));  //
        System.out.println(HkjsUtils.geohash(37.0, 112.3));
        System.out.println(HkjsUtils.geohash(1.0, 112.3));
        System.out.println(HkjsUtils.geohash(37.0, 10.0));
        System.out.println(GeoHash.fromGeohashString("u0000000").getBoundingBox().getSouthWestCorner());
        System.out.println(GeoHash.fromGeohashString("uzzzzzzz").getBoundingBox().getNorthEastCorner());
        System.out.println(GeoHash.fromGeohashString("u").getBoundingBox().getSouthWestCorner());
        System.out.println(GeoHash.fromGeohashString("u").getBoundingBox().getNorthEastCorner());

        System.out.println(GeoHash.fromGeohashString("v").getBoundingBox().getSouthWestCorner());
        System.out.println(GeoHash.fromGeohashString("v").getEasternNeighbour().toBase32());
    }

    @Test
    public void testTimestamp(){
        System.out.println(HkjsUtils.timestampToDate(1588940553000L/5000*5000));
        System.out.println(HkjsUtils.dateTimeToMilli("20200508082230"));

    }

    @Test
    public void testDistance(){
        double[] ds = HkjsUtils.getAround(28.660975, 115.919243, 6.1f);
        System.out.println(ds[0]);
        System.out.println(ds[1]);
        System.out.println(ds[2]);
        System.out.println(ds[3]);
    }
}
