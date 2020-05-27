package com.ruoyi.netty;

public class CPREncoder {

    /**
     * encode latitude and longitude with CPR
     * @param lat latitude
     * @param lon longitude
     * @return int[CPRLatEvent, CPRLonEvent, CPRLatOdd, CPRLonOdd]
     */
    public static int[] encode(double lat, double lon){
        double DLatOdd = 360.0 / 59;
        double DLatEvent = 360.0 / 60;

        int CPRLatOdd = (int) Math.floor(131072.0 * (lat % DLatOdd) / DLatOdd + 0.5);
        int CPRLatEvent = (int) Math.floor(131072.0 * (lat % DLatEvent) / DLatEvent + 0.5);

        double RLatOdd = DLatOdd * (CPRLatOdd / 131072.0 + Math.floor(lat / DLatOdd));
        double RLatEvent = DLatEvent * (CPRLatEvent / 131072.0 + Math.floor(lat / DLatEvent));

        int NLOdd = PositionUtil.cprNL(RLatOdd);
        int NLEvent = PositionUtil.cprNL(RLatEvent);
        double DLonOdd;
        double DLonEvent;
        if (NLOdd - 1 > 0){
            DLonOdd = 360.0 / (NLOdd - 1);
        }else {
            DLonOdd = 360.0;
        }
        if (NLEvent > 0){
            DLonEvent = 360.0 / NLEvent;
        }else {
            DLonEvent = 360.0;
        }

        int CPRLonOdd = (int) Math.floor(131072.0 * (lon % DLonOdd) / DLonOdd + 0.5);
        int CPRLonEvent = (int) Math.floor(131072.0 * (lon % DLonEvent) / DLonEvent + 0.5);

        int[] result = new int[4];
        result[0] = CPRLatEvent;
        result[1] = CPRLonEvent;
        result[2] = CPRLatOdd;
        result[3] = CPRLonOdd;

        return result;
    }

    /**
     * return eventMsg and oddMsg with CPRlat and CPRlon are replaced by new lat and new lon
     * @param msg
     * @param lat
     * @param lon
     * @return String[eventMsg, oddMsg]
     */
    public static String[] replaceLatALon(String msg, String icao24, double lat, double lon){
        if (msg != null && msg.length() == 28){
            msg = msg.replace(msg.substring(2, 8), icao24);
            String msgHeader = PositionUtil.adsbHexMsgToBinaryString(msg.substring(0, 8));
            String msgBinOdd = PositionUtil.adsbHexMsgToBinaryString(msg.substring(8));
            String msgBinEvent = msgBinOdd;

//            System.out.println("original      " + msgHeader + msgBinOdd);

            msgBinOdd = msgBinOdd.substring(0, 21) + "1" + msgBinOdd.substring(22);
            msgBinEvent = msgBinEvent.substring(0, 21) + "0" + msgBinEvent.substring(22);

            int[] latAlon = CPREncoder.encode(lat, lon);
            String[] latAlonString = new String[4];
            for (int i=0; i < 4; i++){
                String binString = Integer.toBinaryString(latAlon[i]);
                int len = binString.length();
                if (len < 17){
                    String zeroString = "";
                    for (int j=0; j < 17 - len; j++){
                        zeroString += "0";
                    }
                    latAlonString[i] = zeroString + binString;
                }else {
                    latAlonString[i] = binString;
                }
            }

            msgBinEvent = msgHeader + msgBinEvent.substring(0, 22) + latAlonString[0] + latAlonString[1] + msgBinEvent.substring(56);
            msgBinOdd = msgHeader + msgBinOdd.substring(0, 22) + latAlonString[2] + latAlonString[3] + msgBinOdd.substring(56);

            String[] msgs = new String[2];
            msgs[0] = PositionUtil.adsbBinaryStringToHexMsg(msgBinEvent);
            msgs[1] = PositionUtil.adsbBinaryStringToHexMsg(msgBinOdd);

            return msgs;
        }
        return null;
    }

    public static void main(String[] args) {
//        int nums[] = CPREncoder.encode(39.76941, 116.66579);
//        System.out.println("(CPRLatEvent, CPRLonEvent) = (" + nums[0] + ", " + nums[1] + ")");
//        System.out.println("(CPRLatOdd, CPRLonOdd) = (" + nums[2] + ", " + nums[3] + ")");
        String[] msgs = CPREncoder.replaceLatALon("8D780581581516121B2A9F651B63", "06a140", 39.92334, 116.81969);

        System.out.println("eventMsg(" + msgs[0].length() + ") " + msgs[0]);
        System.out.println("oddMsg(" + msgs[1].length() + ")   " + msgs[1]);
    }
}
