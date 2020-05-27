package com.ruoyi.netty.handler;

import cn.hutool.core.lang.Console;
import com.ruoyi.common.utils.ADSBTool;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.HkjsUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.netty.model.FeederLinkFrame;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class NettyDataHandler  extends ChannelInboundHandlerAdapter {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HkAdsbDecoder decoder;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FeederLinkFrame frame = (FeederLinkFrame)msg;

//        Console.log(frame.getPayload());

        Console.log(frame.getPayload().length);

//        Console.log(ADSBTool.toHexString(frame.getPayload()));

//        System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
        String data = ADSBTool.toHexString(frame.getPayload()).substring(16,44);
//        ADSBTool.toHexString(frame.getPayload()).substring(16, 44)
        Console.log(data);
        long time = Bytes.toLong(frame.getPayload(), 0, 8);
//        redisTemplate.opsForList().leftPush(HkjsUtils.HK_ADSB_QUEUE, String.format("%d@%s", time, data));
        decoder.decodeMsg(time, data, null);
        decoder.flushIfPending();
        ctx.write(msg);
    }
}
