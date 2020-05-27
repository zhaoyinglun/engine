package com.ruoyi.framework.subscriber;

import com.ruoyi.common.utils.HkjsUtils;
import com.ruoyi.netty.handler.HkAdsbDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by root on 5/18/20.
 */
@Slf4j
@Service
public class RedisAdsbConsumer {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HkAdsbDecoder decoder;

    @Async
    public void receiveForever() {
        while (true) {
            String adsbd = redisTemplate.opsForList().rightPop(HkjsUtils.HK_ADSB_QUEUE);
            if (adsbd != null && !adsbd.isEmpty()) {
                log.debug("接收到redis队列消息： " + adsbd);
                if (adsbd.startsWith("\"")) {
                    adsbd = adsbd.substring(1, adsbd.length());
                }
                if (adsbd.endsWith("\"")) {
                    adsbd = adsbd.substring(0, adsbd.length() - 1);
                }
                String[] msg = adsbd.split("@");
                decoder.decodeMsg(Long.parseLong(msg[0]), msg[1], null);
            }
        }
    }
}
