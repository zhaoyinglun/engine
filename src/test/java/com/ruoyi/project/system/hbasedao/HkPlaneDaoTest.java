package com.ruoyi.project.system.hbasedao;

import com.ruoyi.RuoYiApplication;
import com.ruoyi.project.system.domain.HkAdsbPostion;
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
public class HkPlaneDaoTest {
    @Autowired
    private HkPlaneDao dao;

    @Ignore
    @Test
    public void testAdd() {
        HkAdsbPostion pos1 = new HkAdsbPostion(39.901153564453125, 116.3647394594, 300, false);
        pos1.setTimestamp(1588938437177L);
        pos1.setIcao("3f3f3f");
        boolean isok = dao.putPostion(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }

        pos1 = new HkAdsbPostion(39.901012, 116.364512, 302, false);
        pos1.setTimestamp(1588938438222L);
        pos1.setIcao("3f3f3f");
        isok = dao.putPostion(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }

        pos1 = new HkAdsbPostion(39.900993735103285, 116.36444091796875, 300, false);
        pos1.setTimestamp(1588938440186L);
        pos1.setIcao("3f3f3f");
        isok = dao.putPostion(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }
        pos1 = new HkAdsbPostion(38.62718485169491, 113.7928466796875, 300, false);
        pos1.setTimestamp(1588938442191L);
        pos1.setIcao("3f3f3f");
        isok = dao.putPostion(pos1);
        if (!isok) {
            throw new RuntimeException("add failed");
        }
        pos1 = new HkAdsbPostion(37.940679, 112.487580, 300, false);
        pos1.setTimestamp(1588938446207L);
        pos1.setIcao("3f3f3f");
        isok = dao.putPostion(pos1);

        System.out.println("add pos " + (isok ? "OK" : "Failed"));
    }

    @Ignore
    @Test
    public void testFind() throws IOException {
        List<HkAdsbPostion> res = dao.findBetweenTime("3f3f3f", 1588938437178L, 1588938446207L);
        for (HkAdsbPostion p : res) {
            System.out.println(p);
        }
    }

    @Ignore
    @Test
    public void findByIcao() throws IOException {
        List<HkAdsbPostion> res = dao.findByIcao("3f3f3f");
        for (HkAdsbPostion p : res) {
            System.out.println(p);
        }
    }

}
