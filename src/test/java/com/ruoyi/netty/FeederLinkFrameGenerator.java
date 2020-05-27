package com.ruoyi.netty;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.utils.ADSBTool;
import com.ruoyi.netty.model.FeederLinkFrame;
import com.ruoyi.netty.model.FeederLinkFrameHeader;
import org.junit.Assert;


import java.io.*;

import static org.junit.Assert.assertEquals;

public  class  FeederLinkFrameGenerator {

    private static FeederLinkFrameGenerator INSTANCE;

    public static FeederLinkFrameGenerator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FeederLinkFrameGenerator();
        }
        return INSTANCE;
    }

    public FeederLinkFrame generateFrameData() throws IOException {
        FeederLinkFrame frame = FeederLinkFrame.builder()
                .header(
                        FeederLinkFrameHeader.builder()
                                .magicCode(0x1ACFFC1D)
                                .exchangeTag(generateExchangeTag())
                                .frameType(generateFrameType())
                                .priorityLevel(generatePriorityLevel())
                                .build()
                )
                .securitySign(generateSecuritySign())
                .payload(generatePayload())
                .crc(generateCRC())
                .build();

        Console.log(frame.toString());
        assertEquals(168,frame.toBytes().length);

        return frame;
    }

    private int generateExchangeTag() {
        return 0xC0                             // 信关站编号
                &
                RandomUtil.randomInt(32);
    }
    private byte generateFrameType() {
        return (byte) 0x90;     // ads-b
    }
    private byte generatePriorityLevel () {
        return (byte) RandomUtil.randomInt(8);  // 0b0110   ads-b
    }

    private byte[] generateSecuritySign() {
        return RandomUtil.randomBytes(16);
    }

    private byte[] generatePayload() throws IOException {


        Assert.assertTrue(FileUtil.file("position.txt").exists());

        try (
                BufferedReader br = new BufferedReader(new FileReader(FileUtil.file("position.txt")));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            String line;
            int n = 0;
            while ((line = br.readLine()) != null && n != 10){


                assert (line.getBytes().length == 28) : "一行数据必须28" ;
                assert (ADSBTool.hexStringToByteArray(line).length == 14) : "一行数据必须14个字节" ;
                outputStream.write(ADSBTool.hexStringToByteArray(line));
                n++;
            }

            byte data[] = outputStream.toByteArray();
            assert (data.length == 140) : "总共数据 140个字节";
            return data;
        }
        //return RandomUtil.randomBytes(140);
    }

    private short generateCRC() {
        return (short)RandomUtil.randomInt(65535);
    }
}
