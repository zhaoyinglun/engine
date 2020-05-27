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
public class FeederLinkFrame {
    private FeederLinkFrameHeader header;   // 10 byte
    private byte[] securitySign;        // 16 byte
    private byte[] payload;             // 140 byte
    private short  crc;                 // 2 byte

    public byte[] toBytes() throws IOException {
        try (
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ){
            outputStream.write(header.toBytes());
            outputStream.write(securitySign);
            outputStream.write(payload);
            outputStream.write(ByteBuffer.allocate(2).putShort(crc).array());

            byte data[] = outputStream.toByteArray( );

            assert (data.length == 168) : "二进制输出的馈电链路的帧长度必须为 168 ，当前为 " + data.length + " \n";

            return data;
        }
    }
}
