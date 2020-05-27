package com.ruoyi.netty;

import lombok.AllArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@AllArgsConstructor
public class ReceiveServerTest {

//    TODO: 2020/4/29 增加spring test context

//    @Autowired
//    private ReceiveServer server;
//
//
//
//    @Before
//    public void setupNettyServer(){
//        Assert.assertNotNull(server);
//        server.start();
//    }

    @Ignore
    @Test(timeout = 1000)
    public void runClient() throws Exception {
        int port = 51000;
        new NettyTestClient().connect(port);
    }
}