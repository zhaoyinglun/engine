package com.ruoyi.netty.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


@Builder
@ToString
@Data
public class FeederLinkFrameHeader {
    private int     magicCode;       // 4 byte
    private int     exchangeTag;     // 4 byte
    private byte    frameType;       // 1 byte
    private byte    priorityLevel;   // 1 byte

    public byte[] toBytes() throws IOException {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(ByteBuffer.allocate(4).putInt(magicCode).array());
            outputStream.write(ByteBuffer.allocate(4).putInt(exchangeTag).array());
            outputStream.write(frameType);
            outputStream.write(priorityLevel);

            byte data[] = outputStream.toByteArray( );

            assert (data.length == 10) : "二进制输出的馈电链路的帧头长度必须为 10 ，当前为 " + data.length + " \n";

            return data;
        }
    }
}
