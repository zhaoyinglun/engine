package com.ruoyi.framework.subscriber;

import com.ruoyi.netty.handler.HkAdsbDecoder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j
@NoArgsConstructor
public class ADSBDataHandler {

    @Autowired
    private HkAdsbDecoder decoder;

    public void onMessage(String adsbd) {
        log.debug("接收到redis消息数据： " + adsbd);
        if(adsbd.startsWith("\"")){
            adsbd = adsbd.substring(1, adsbd.length());
        }
        if(adsbd.endsWith("\"")){
            adsbd = adsbd.substring(0, adsbd.length()-1);
        }
        String[] msg = adsbd.split("@");
        decoder.decodeMsg(Long.parseLong(msg[0]), msg[1], null);
    }
}
