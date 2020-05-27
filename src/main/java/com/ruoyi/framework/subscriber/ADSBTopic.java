package com.ruoyi.framework.subscriber;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class ADSBTopic extends ChannelTopic {
    private static final String ADSB_CHANNEL = "adsb";

    public ADSBTopic () {
        super(ADSB_CHANNEL);
    }
}