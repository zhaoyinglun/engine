package com.ruoyi.project.system.hbasedao;

import com.ruoyi.RuoYiApplication;
import com.ruoyi.common.utils.ADSBTool;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.HkjsUtils;
import com.ruoyi.project.system.domain.HkAdsbPostion;
import com.ruoyi.project.system.domain.HkAdsbSpeed;
import com.ruoyi.project.system.domain.vo.AirplaneEntityVo;
import org.apache.hadoop.hbase.client.Put;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 5/10/20.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RuoYiApplication.class})
public class HkGeohashDaoTest {

    @Autowired
    HkGeohashDao dao;

    @Ignore
    @Test
    public void addPlanes(){
        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/cityLocation.txt"))) {
            String line = null;
            String[] arr = null;
            long time = 1589165698523L;
            byte icao = 0x00;
            int i = 0;
            while((line = br.readLine()) != null){
                arr = line.split(",");
//                putPlane(time, Double.parseDouble(arr[2]),
//                        Double.parseDouble(arr[1]), 800+50*i,
//                        "EFEF"+ADSBTool.toHexString(icao));
                putPlaneSpeed(time, Double.parseDouble(arr[2]),
                        Double.parseDouble(arr[1]), icao, 500+10*i,
                        "EFEF"+ADSBTool.toHexString(icao));
                icao += 1;
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void putPlane(long time, double lat, double lon, float alt, String icao){
        HkAdsbPostion pos1 = new HkAdsbPostion(lat, lon, alt, false);
        pos1.setTimestamp(time);
        pos1.setIcao(icao);
        dao.putHashPosition(pos1);
    }

    private void putPlaneSpeed(long time, double lat, double lon, float direction, float speed, String icao){
        HkAdsbPostion pos1 = new HkAdsbPostion(lat, lon, 0, false);
        pos1.setTimestamp(time);
        pos1.setIcao(icao);
        HkAdsbSpeed spd = new HkAdsbSpeed();
        spd.setTrack(direction);
        spd.setGspeed(speed);
        spd.setTimestamp(time);
        spd.setIcao(icao);
        spd.setLat(lat);
        spd.setLon(lon);
        dao.putHashSpeed(spd);
    }

    @Ignore
    @Test
    public void testLargePut(){
        int cnt = 10000;
        int loop = cnt/225;
        HkAdsbPostion pos1 = null;
        byte dicao2 = 0x00;
        long stime = DateUtils.getNowDate().getTime();
        List<Put> puts = new ArrayList<>();
        for(int i=0; i< loop; i++){
            pos1 = new HkAdsbPostion(39.901153564453125, 116.3647394594, 300, false);
            pos1.setTimestamp(1588938437177L);
            byte dicao3 = 0x00;
            for(int j=0; j<255; j++){
                String icao = "AF" + ADSBTool.toHexString(dicao2) + ADSBTool.toHexString(dicao3);
                dicao3 += 1;
                pos1.setIcao(icao);
//                dao.putHashPosition(pos1);
                puts.add(dao.convertPosition(pos1));
            }
            dicao2 += 1;
        }
        dao.putPuts(puts);
        long etime = DateUtils.getNowDate().getTime();
        System.out.println(String.format("Time spent: %dms", etime-stime));
    }

    @Ignore
    @Test
    public void testPut(){
        // geohash = wx4dzzy1n
        HkAdsbPostion pos1 = new HkAdsbPostion(39.901153564453125, 116.3647394594, 300, false);
        pos1.setTimestamp(1588938437177L);
        pos1.setIcao("3f3f3f");
        boolean isok = dao.putHashPosition(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }
        pos1 = new HkAdsbPostion(39.901012, 116.364512, 302, false);
        pos1.setTimestamp(1588938438222L);
        pos1.setIcao("3f3f3f");
        isok = dao.putHashPosition(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }

        pos1 = new HkAdsbPostion(39.900993735103285, 116.36444091796875, 300, false);
        pos1.setTimestamp(1588938440186L);
        pos1.setIcao("3f3f3f");
        isok = dao.putHashPosition(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }
        pos1 = new HkAdsbPostion(38.62718485169491, 113.7928466796875, 300, false);
        pos1.setTimestamp(1588938436191L);
        pos1.setIcao("ef3f3f");
        isok = dao.putHashPosition(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }
        pos1 = new HkAdsbPostion(37.940679, 112.487580, 300, false);
        pos1.setTimestamp(1588938438207L);
        pos1.setIcao("ef3f3f");
        isok = dao.putHashPosition(pos1);

        System.out.println("add pos " + (isok ? "OK" : "Failed"));
    }

    @Ignore
    @Test
    public void testQuery() throws IOException {
        String geohash2 = HkjsUtils.geohash(39.9999966, 116.36666666); // wx4excz8g
        String geohash1 = HkjsUtils.geohash(36.9999966, 111.36666666); // wqx4qpwsx
        System.out.println(geohash1+"---"+geohash2);
        Map<String, AirplaneEntityVo> map = dao.findByArea(1588938435177L, geohash1, geohash2);
        for(String icao: map.keySet()){
            System.out.println(icao+"---->");
            System.out.println("  "+map.get(icao));
        }
    }
}
