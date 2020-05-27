package com.ruoyi.netty.handler;

import com.ruoyi.netty.model.FeederLinkFrame;
import com.ruoyi.netty.model.FeederLinkFrameHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.List;

@Slf4j
@Component
@ChannelHandler.Sharable
public class FeederLinkFrameDecoder  extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer, List<Object> list) throws Exception {

        if (buffer.readableBytes() < 168) {
            return;
        }
        try {
            int magicCode = buffer.readInt();
            if(magicCode != 0x1ACFFC1D){
                log.warn("独特字匹配失败，非馈电链路帧数据格式，数据丢弃");
                return;
            }
            int exchangeTag = buffer.readInt();
            ByteBuf frameType = buffer.readBytes(1);
            ByteBuf priorityLevel = buffer.readBytes(1);

            ByteBuf securitySign = buffer.readBytes(16);
            byte[] securitySignBytes = new byte[securitySign.readableBytes()];
            securitySign.readBytes(securitySignBytes);


            ByteBuf payload = buffer.readBytes(140);
            byte[] payLoadBytes = new byte[payload.readableBytes()];
            payload.readBytes(payLoadBytes);

            short crc = buffer.readShort();

            FeederLinkFrame frame = FeederLinkFrame.builder()
                    .header(FeederLinkFrameHeader.builder()
                            .magicCode(magicCode)
                            .exchangeTag(exchangeTag)
                            .frameType(frameType.readByte())
                            .priorityLevel(priorityLevel.readByte())
                            .build())
                    .securitySign(securitySignBytes)
                    .payload(payLoadBytes)
                    .crc(crc)
                    .build();
            log.debug(frame.toString());
            list.add(frame);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
