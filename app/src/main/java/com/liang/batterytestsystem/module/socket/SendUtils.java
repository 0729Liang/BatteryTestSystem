package com.liang.batterytestsystem.module.socket;

import android.content.Context;

import com.liang.liangutils.utils.LLogX;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SendUtils {

    public static final  String NET_ALL   = "255.255.255.255"; // 发给整个局域网
    public static final  String NET_GROUP = "192.168.100.255";// 发给同一网段
    public static final  String NET_OBJ   = "192.168.100.8"; // 发给指定IP
    private static final int    SEND_PORT = 61001; // server端口
    private static       String IP        = NET_GROUP; // server IP

    private static InetAddress mAddress; // 服务器网址
    private static DatagramSocket socket = null;
    private static DatagramPacket msg    = null;

    public static void sendCommand(final byte[] content) {
        //初始化socket
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            mAddress = InetAddress.getByName(IP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //创建线程发送信息
        new Thread() {
            private byte[] sendBuf;

            @Override
            public void run() {
                sendBuf = content;
                msg = new DatagramPacket(sendBuf, sendBuf.length, mAddress, SEND_PORT);
                try {
                    socket.send(msg);
                    socket.close();
                    LLogX.e("客户端发送：" + new String(content, "UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void SendUtils(final Context context, final String content) {
        //初始化socket
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            mAddress = InetAddress.getByName(IP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //创建线程发送信息
        new Thread() {
            private byte[] sendBuf;

            @Override
            public void run() {
                try {
                    sendBuf = content.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                DatagramPacket msg = new DatagramPacket(sendBuf, sendBuf.length, mAddress, SEND_PORT);
                try {
                    socket.send(msg);
                    socket.close();
                    LLogX.e("zziafyc", "已将内容发送给了AIUI端内容为：" + content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
