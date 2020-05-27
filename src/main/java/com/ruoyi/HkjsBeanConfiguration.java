package com.ruoyi;

import com.ruoyi.project.system.service.IHkAirplaneService;
import com.ruoyi.project.system.service.IHkOptionService;
import com.ruoyi.project.system.service.impl.HkAirplaneServiceImpl;
import com.ruoyi.project.system.service.impl.HkOptionServiceImpl;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.opensky.libadsb.ModeSDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by root on 4/26/20.
 */
@Configuration
public class HkjsBeanConfiguration {

    @Value("${hbase.url}")
    private String hbaseUrl;

    @Value("${hbase.timeout}")
    private String hbaseTimeout;

    @Bean
    public Connection getConnection() throws IOException {
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", hbaseUrl);
        config.set(HConstants.HBASE_RPC_TIMEOUT_KEY, hbaseTimeout);
        return ConnectionFactory.createConnection(config);
    }

    @Bean
    public ModeSDecoder getModeSDecoder() {
        return new ModeSDecoder();
    }

    @Bean(name = "hkAirplaneService")
    public IHkAirplaneService getHkAirplaneService(@Autowired Connection connection){
        HkAirplaneServiceImpl service = new HkAirplaneServiceImpl(connection);
        return service;
    }

}
