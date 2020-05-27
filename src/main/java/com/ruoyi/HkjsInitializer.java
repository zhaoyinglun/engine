package com.ruoyi;

import com.ruoyi.common.utils.DBConfigUtils;
import com.ruoyi.framework.subscriber.RedisAdsbConsumer;
import com.ruoyi.netty.handler.HkAdsbDecoder;
import com.ruoyi.project.system.service.IHkAirplaneService;
import com.ruoyi.project.system.service.IHkOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by root on 4/26/20.
 */
@Component
public class HkjsInitializer implements ApplicationRunner{

    @Autowired
    @Qualifier("hkAirplaneService")
    IHkAirplaneService hkAirplaneService;


    @Autowired
    RedisAdsbConsumer adsbConsumer;

    @Autowired
    HkAdsbDecoder hkAdsbDecoder;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        // create hbase table
        System.out.println("HkjsInitializer----started----");
        hkAirplaneService.createTables();
        DBConfigUtils.getInstance().setOptionList();
        adsbConsumer.receiveForever();
        hkAdsbDecoder.flush();
        System.out.println("HkjsInitializer----finished----");
    }
}
