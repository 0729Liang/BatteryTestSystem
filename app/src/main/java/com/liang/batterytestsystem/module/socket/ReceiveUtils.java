package com.liang.batterytestsystem.module.socket;

import com.liang.batterytestsystem.device.DeviceEvent;
import com.liang.batterytestsystem.module.config.UdpInfoStorage;
import com.liang.batterytestsystem.utils.DigitalTrans;
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
    private static final int PHONE_PORT  = 54915;//手机端口号
    private static       int SERVER_PORT = UdpInfoStorage.getClientListenPort();//手机端口号

    private static DatagramSocket socket;
    private static DatagramPacket packet;
    private static volatile boolean stopReceiver = false;


    public static void receiveMessage() {
        LLogX.e("等待接收");

        SERVER_PORT = UdpInfoStorage.getClientListenPort();//手机端口号
        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new DatagramSocket(SERVER_PORT);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                byte[] receBuf = new byte[1024];
                packet = new DatagramPacket(receBuf, receBuf.length);
                while (!stopReceiver) {
                    try {
                        socket.receive(packet);
                        //String receive = new String(packet.getData(), 0, packet.getLength(), "utf-8");
                        String receive = DigitalTrans.byte2hex(packet.getData());
                        LLogX.e("huawei", "收到" + receive.length() / 2 + "字节 内容:" + receive);
                        DeviceEvent.postRecvMsg(receive);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
