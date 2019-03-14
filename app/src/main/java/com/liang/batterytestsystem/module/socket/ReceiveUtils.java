package com.liang.batterytestsystem.module.socket;

import com.liang.liangutils.utils.LLogX;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author : Amarao
 * CreateAt : 12:49 2019/3/14
 * Describe :
 */
public class ReceiveUtils {
    private static final int PHONE_PORT = 54915;//手机端口号

    private static DatagramSocket socket;
    private static DatagramPacket packet;
    private static volatile boolean stopReceiver = false;


    public static void receiveMessage() {
        LLogX.e("等待接收");
        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new DatagramSocket(PHONE_PORT);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                byte[] receBuf = new byte[1024];
                packet = new DatagramPacket(receBuf, receBuf.length);
                while (!stopReceiver) {
                    try {
                        socket.receive(packet);
                        String receive = new String(packet.getData(), 0, packet.getLength(), "utf-8");
                        LLogX.e("huawei", "收到的内容为：" + receive);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
