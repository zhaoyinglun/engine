package com.ruoyi.netty;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import cn.hutool.core.lang.Console;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class OpenSkyDataTest {

    private String host = "127.0.0.1";
    private int port = 51000;
    private String queryUrlPrefix = "https://opensky-network.org/api/states/all?icao24=";
    private byte[] frame0 = new byte[168];

    // 需要去 https://opensky-network.org/ 获得最新的数据
    private String airplaneICAO = "396668";

    private BlockingQueue<byte[]> blockingDeque = new LinkedBlockingQueue<>();

    @Before
    public void init() {

        frame0[0] = 0x1A;
        frame0[1] = (byte) 0xCF;
        frame0[2] = (byte) 0xFC;
        frame0[3] = 0x1D;

        // Swap labels
        frame0[4] = 0;
        frame0[5] = 0;
        frame0[6] = 0;
        frame0[7] = (byte) 0b11001011;

        // frame type
        frame0[8] = (byte) 0b10010000;

        // priority
        frame0[9] = (byte) 0b00000110;

        // enc_dec flag, unknown
        for (int i = 10; i <= 25; i++) {
            frame0[i] = 0;
        }

        // CRC
        frame0[166] = 0;
        frame0[167] = 0;
    }


    private String getFlightFromOpensky(String icao24){
        String httpurl = queryUrlPrefix + icao24;

        URL url;
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\n");
                }
                result = sbf.toString();
                return result;
            }

            Console.log(connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }
        return null;
    }

    private void generateFakePositionFrame(String hexMsg, String icao24, float lat, float lon) {
        String[] msgs = CPREncoder.replaceLatALon(hexMsg, icao24, lat, lon);
        String eventMsg = msgs[0];
        String oddMsg = msgs[1];

        byte[] eventFrame = Arrays.copyOf(frame0, 168);
        for (int i = 0; i + 2 <= eventMsg.length(); i += 2) {
            eventFrame[i / 2 + 26] = (byte) Integer.parseUnsignedInt(eventMsg.substring(i, i + 1) + eventMsg.substring(i + 1, i + 2), 16);
        }
        byte[] oddFrame = Arrays.copyOf(frame0, 168);
        for (int i = 0; i + 2 <= oddMsg.length(); i += 2) {
            oddFrame[i / 2 + 26] = (byte) Integer.parseUnsignedInt(oddMsg.substring(i, i + 1) + oddMsg.substring(i + 1, i + 2), 16);
        }
        try {
            blockingDeque.put(eventFrame);
            blockingDeque.put(oddFrame);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addFakePositionFrame(String icao24) {

        new Thread(() -> {
            while (true) {
                String result = getFlightFromOpensky(icao24);
                if (result != null){
                    Console.log("result = " + result);

                    // Process all flight events.
                    JSONObject j = (JSONObject)  JSONValue.parse(result);
                    long timestamp = (long) j.get("time");
                    List<List> states = (List<List>) j.get("states");
                    for (List l : states){
                        String tmpicao24 = (String) l.get(0);
                        float lat = ((Double)l.get(5)).floatValue();
                        float lon = ((Double)l.get(6)).floatValue();
                        Console.log(timestamp + " " + lat + " " + lon);
                        generateFakePositionFrame("8D780581581516121B2A9F651B63", tmpicao24, lat, lon);
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void run () {
        Socket client_socket = null;
        OutputStream os = null;
        try {
            client_socket = new Socket(host, port);
            os = client_socket.getOutputStream();

            addFakePositionFrame(airplaneICAO);

            while (true) {
                os.write(blockingDeque.take());
                os.flush();
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (client_socket != null) {
                    client_socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Ignore
    @Test
    public void test() {
        run();
    }

}
