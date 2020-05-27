package com.ruoyi.framework.task;

import com.ruoyi.project.system.domain.HkAdsbBase;
import com.ruoyi.project.system.domain.HkAdsbIdent;
import com.ruoyi.project.system.domain.HkAdsbPostion;
import com.ruoyi.project.system.domain.HkAdsbSpeed;
import com.ruoyi.project.system.hbasedao.HkFlightDao;
import com.ruoyi.project.system.hbasedao.HkGeohashDao;
import com.ruoyi.project.system.hbasedao.HkPlaneDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by shihr on 5/21/20.
 */
@Component("readDirTask")
@Slf4j
public class HkjsCollectFromDir {

    @Value("${flightinfodir.csv}")
    private String csvdir;

    @Autowired
    private HkPlaneDao planeDao;
    @Autowired
    private HkGeohashDao geohashDao;
    @Autowired
    private HkFlightDao flightDao;

    private DoubleBuffer doubleBuffer = new DoubleBuffer();

    @SuppressWarnings("unchecked")
    private static <T> T cast(String v, Class<T> clazz, T dv) {
        if (v == null || v.length() == 0) {
            return dv;
        }

        if (clazz == Double.class){
            return (T) Double.valueOf(v);
        }
        else if (clazz == Float.class){
            return (T) Float.valueOf(v);
        }
        else if (clazz == Integer.class){
            return (T) Integer.valueOf(v);
        }
        else if (clazz == String.class){
            return (T) v;
        }

        return dv;
    }

    private void collectPlanesInfoFromCsvFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();  // skip title
            String line;
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");

                SimpleDateFormat sd;
                Date date;
                try {
                    sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    date = sd.parse(info[0]);
                } catch (ParseException e) {
                    sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        date = sd.parse(info[0]);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                        continue;
                    }
                }
                long timestamp = date.getTime();

                String icao24 = info[1];
                while (icao24.length() < 6) {
                    icao24 = "0" + icao24;
                }
                boolean isValid = Pattern.matches("^\\w{6}$", icao24);
                if (!isValid){
                    continue;
                }

                Double lat = cast(info[2], Double.class, null);

                Double lon = cast(info[3], Double.class, null);

                Float alt = info[4].length() != 0 ? Float.parseFloat(info[4]) * 0.3048f : null;

                Float velocity = cast(info[5], Float.class, null);

                Float track = cast(info[6], Float.class, null);

                String callsign = cast(info[7], String.class, null);

                Float vertical_rate = cast(info[10], Float.class, null);

                String plane_type = cast(info[12], String.class, null);

                boolean is_surface = velocity != null && velocity == 0f;

                if (callsign != null){
                    HkAdsbIdent ident = new HkAdsbIdent();
                    ident.setTimestamp(timestamp);
                    ident.setIcao(icao24);
                    ident.setCallsign(callsign);
                    if (plane_type != null){
                        ident.setPlaneType(plane_type);
                    }
                     saveToBuffer(ident);
                }

                if (lat != null && lon != null && alt != null){
                    HkAdsbPostion position = new HkAdsbPostion(lat, lon, alt, is_surface);
                    position.setTimestamp(timestamp);
                    position.setIcao(icao24);
                    if (track != null) {
                        position.setTrack(track);
                    }
                    if (velocity != null){
                        position.setGspeed(velocity);
                    }
                     saveToBuffer(position);
                }

                if (velocity != null){
                    HkAdsbSpeed speed = new HkAdsbSpeed();
                    speed.setTimestamp(timestamp);
                    speed.setIcao(icao24);
                    if (lat != null){
                        speed.setLat(lat);
                    }
                    if (lon != null){
                        speed.setLon(lon);
                    }
                    if (track != null) {
                        speed.setTrack(track);
                    }
                    if (is_surface){
                        speed.setGspeed(velocity);
                    }else {
                        speed.setSpeed(velocity);
                        if (vertical_rate != null){
                            speed.setVspeed(vertical_rate);
                        }
                    }
                     saveToBuffer(speed);
                }
            }
            flush(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void collectPlanesInfoFromCsvDir() {
        File directory = new File(csvdir);
        if (directory.isDirectory()) {
            Set<File> files = new HashSet<>();
            File[] tmpFiles = directory.listFiles();
            if (tmpFiles == null) {
                log.error("cannot get files in " + directory);
                return;
            }
            for (File tmpfile : tmpFiles) {
                if (tmpfile.getName().endsWith(".csv")) {
                    files.add(tmpfile);
                }
            }

            Thread flushThread = new Thread(() -> flush(false));

            flushThread.setDaemon(true);
            flushThread.start();

            files.forEach(this::collectPlanesInfoFromCsvFile);
        }else {
            log.error(directory + " is not a directory!");
        }
    }

    private void saveToBuffer(HkAdsbBase hkAdsbBase){
        synchronized (this){
            doubleBuffer.putToList(hkAdsbBase);
        }
    }

    private void flush(boolean force){
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(doubleBuffer.isInFlush() || doubleBuffer.getCurLen() == 0){
                continue;
            }
            if (doubleBuffer.getCurLen() < 1000 && !force) {
                continue;
            }

            synchronized (this) {
                if(doubleBuffer.isInFlush())
                    continue;
                if (doubleBuffer.getCurLen() < 1000 && !force) {
                    continue;
                }
                doubleBuffer.setInFlush(true);
                doubleBuffer.exchange();
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

    /**
     * 因为刷写数据库比较慢，为了避免数据接收受到刷写的阻塞，
     * 采用双缓冲机制，解析后的数据放入curBuf，达到刷写条件时将curBuf和syncBuf做交换
     * 交换后将syncBuf刷写入数据库，并且清空syncBuf。刷写可采用多线程来提高效率
     */
    private class DoubleBuffer{
        private LinkedList<HkAdsbBase> curBuf = new LinkedList<>();
        private LinkedList<HkAdsbBase> syncBuf = new LinkedList<>();
        private boolean inFlush = false;

        private void putToList(HkAdsbBase hkAdsbBase){
            curBuf.add(hkAdsbBase);
        }

        private void flushToDb(){
            List<Put> hashList = new ArrayList<>();
            List<Put> planeList = new ArrayList<>();
            List<Put> flightList = new ArrayList<>();
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
                    flightList.add(flightDao.convertIdent((HkAdsbIdent) hkAdsbBase));
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

        private void exchange(){
            LinkedList<HkAdsbBase> tmp = curBuf;
            curBuf = syncBuf;
            syncBuf = tmp;
        }

        private int getCurLen(){
            return curBuf.size();
        }

        private boolean isInFlush() {
            return inFlush;
        }

        private void setInFlush(boolean inFlush) {
            this.inFlush = inFlush;
        }
    }
}
