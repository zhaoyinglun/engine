package com.ruoyi.netty;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;


public class PositionUtil {
    public static String adsbHexMsgToBinaryString(String msg){
        String binaryString = "";

//        byte[] byteArray = tools.hexStringToByteArray(msg);
//        HexUtils.fromHexString(msg);
        byte[] byteArray = HexBin.decode(msg);
        for(byte oneByte:byteArray){
            String oneByteBinaryString = Integer.toBinaryString(Byte.toUnsignedInt(oneByte));
            int len = oneByteBinaryString.length();
            if (len < 8){
                String zeroString = "";
                for (int i=0; i < 8 - len; i++){
                    zeroString += "0";
                }
                oneByteBinaryString = zeroString + oneByteBinaryString;
            }
            binaryString += oneByteBinaryString;
        }
        return binaryString;
    }

    public static String adsbBinaryStringToHexMsg(String binaryString){
        int len = binaryString.length() / 4;
        byte[] bytes = new byte[len];
        String hexString = "";

        for (int i=0; i < len; i ++){
            bytes[i] = (byte)Integer.parseUnsignedInt(binaryString.substring(i*4, (i+1)*4), 2);
        }
        for (byte b : bytes){
            hexString += Integer.toHexString(Byte.toUnsignedInt(b));
        }
        return hexString;
    }

    public static int cprNL(double lat) {
        if (lat == 0){
            return 59;
        }

        if (lat == 87 || lat == -87){
            return 2;
        }

        if (lat > 87 || lat < -87){
            return 1;
        }

        int nz = 15;
        double a = 1 - Math.cos(Math.PI / (2 * nz));
        double b = Math.pow(Math.cos(Math.PI / 180.0 * Math.abs(lat)), 2);
        double nl = 2 * Math.PI / (Math.acos(1 - a/b));
        return (int)Math.floor(nl);
    }
}
