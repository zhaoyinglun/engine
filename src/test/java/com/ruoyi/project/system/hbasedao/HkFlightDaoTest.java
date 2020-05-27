package com.ruoyi.project.system.hbasedao;

import com.ruoyi.RuoYiApplication;
import com.ruoyi.common.utils.ADSBTool;
import com.ruoyi.project.system.domain.HkAdsbIdent;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by root on 5/10/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RuoYiApplication.class})
public class HkFlightDaoTest {

    @Autowired
    HkFlightDao dao;

    @Ignore
    @Test
    public void testAdd(){
        boolean isok = true;
        isok = saveFlights("ffeecc", "hkk223", 158123456L, null, 10);
        isok = saveFlights("eeeecc", "hkk223", 158145456L, null, 10);
        String res = isok?"add ok!":"add failed!";
        System.out.println(res);
    }

    @Ignore
    @Test
    public void testAddWithType(){
        boolean isok = true;
        isok = saveFlights("33eecc", "abcd223", 158128456L, "BOING737", 10);
        isok = saveFlights("33eecc", "abcd223", 158138456L, "AIRBUS310", 10);
        isok = saveFlights("44eecc", "abcd223", 158148456L, "AIRBUS310", 10);
        String res = isok?"add ok!":"add failed!";
        System.out.println(res);
    }

    private boolean saveFlights(String icao, String flight, long stimeSec, String type, int count){
        HkAdsbIdent obj = new HkAdsbIdent();
        obj.setCallsign(flight);
        obj.setIcao(icao);
        if(type != null)
            obj.setPlaneType(type);
        boolean isok = true;
        for(int timeOffset = 0; timeOffset<count && isok; timeOffset+=10){
            long time = (stimeSec + timeOffset) * 1000;
            obj.setTimestamp(time);
            isok = dao.putFlight(obj);
        }
        return isok;
    }

    @Ignore
    @Test
    public void testAddPlaneType(){
        String icao;
        String flightPre = "CAU1";
        byte bi = 0x00;
        String[] types = {"AIRBUSA321-271N NEO", "BOING737"};
        for(int i=0; i<35; i++){
            icao = "EFEF"+ ADSBTool.toHexString(bi);
            String flight = flightPre + ADSBTool.toHexString(bi);
            String type = types[i%2];
            saveFlights(icao, flight, 1589165698L, type, 1);
            bi++;
        }
    }

    @Ignore
    @Test
    public void testFindByFlight() throws IOException {
        List<HkAdsbIdent> res = dao.findByFlight("abcd223");
        for (HkAdsbIdent obj : res){
            System.out.println(String.format("%s:%s:%d-->%s", obj.getCallsign(), obj.getIcao(), obj.getTimestamp(), obj.getPlaneType()));
        }
    }

    @Ignore
    @Test
    public void testFindByIcao() throws IOException {
        List<HkAdsbIdent> res = dao.findByIcao("33eecc");
        // List<HkAdsbIdent> res = dao.findByIcao("ffeecc");
        for (HkAdsbIdent obj : res){
            System.out.println(String.format("%s:%s:%d-->%s", obj.getCallsign(), obj.getIcao(), obj.getTimestamp(), obj.getPlaneType()));
        }
    }

    @Ignore
    @Test
    public void getHisByIcao() throws IOException {
        // HkAdsbIdent obj = dao.findByIcaoAndTime("33eecc", 158138456000L);
        HkAdsbIdent obj = dao.getHisByIcao("33eecc", 158128456000L);
        printFlightInfo(obj); // BCD223 :33eecc:158128456000-->BOING737
    }

    @Ignore
    @Test
    public void getLastestByFlight() throws IOException {
        printFlightInfo(dao.getLastestByFlight("abcd223"));
        // BCD223 :44eecc:158148456000-->AIRBUS310
    }

    @Ignore
    @Test
    public void getHisByFlight() throws IOException {
        printFlightInfo(dao.getHisByFlight("abcd223", 158128456000L));
        // BCD223 :33eecc:158128456000-->BOING737
    }


    @Ignore
    @Test
    public void getLastestByIcao() throws IOException {
        printFlightInfo(dao.getLastestByIcao("33eecc"));
        // "33eecc", "abcd223", 158138456L, "AIRBUS310"
    }

    private void printFlightInfo(HkAdsbIdent obj){
        if(obj == null) {
            System.out.println("null");
        } else {
            System.out.println(String.format("%s:%s:%d-->%s", obj.getCallsign(), obj.getIcao(), obj.getTimestamp(), obj.getPlaneType()));
        }
    }
}
