package com.liang.batterytestsystem.module.config;

import com.liang.batterytestsystem.constant.DeviceKey;
import com.liang.batterytestsystem.utils.LNetwork;
import com.liang.liangutils.init.LCommon;
import com.liang.liangutils.mgrs.LKVMgr;

/**
 * @author : Amarao
 * CreateAt : 19:23 2019/3/14
 * Describe :
 */
public class UdpInfoStorage {

    private static String clientIp = LNetwork.getIPAddress(LCommon.getContext()); // 本机IP
    private static int clientListenPort = 61001; // 监听端口
    private static String serverIp = "192.168.43.8"; // 服务器IP
    private static int clientSendPort = 61000; // 发送端口


    public static String getClientIp() {
        return LKVMgr.getInstance().getString(DeviceKey.KEY_CLIENT_IP, clientIp);
    }

    // 客户端信息存储
    public static void setClientIp(String ip) {
        LKVMgr.getInstance().putString(DeviceKey.KEY_CLIENT_IP, ip);
    }

    public static int getClientListenPort() {
        return LKVMgr.getInstance().getInt(DeviceKey.KEY_CLIENT_LISTEN_PORT, clientListenPort);
    }

    public static void setClientListenPort(int port) {
        LKVMgr.getInstance().putInt(DeviceKey.KEY_CLIENT_LISTEN_PORT, port);
    }

    public static String getServerIp() {
        return LKVMgr.getInstance().getString(DeviceKey.KEY_SERVER_IP, serverIp);
    }

    // 服务端信息存储
    public static void setServerIp(String ip) {
        LKVMgr.getInstance().putString(DeviceKey.KEY_SERVER_IP, ip);
    }

    public static int getClientSendPort() {
        return LKVMgr.getInstance().getInt(DeviceKey.KEY_CLIENT_SEND_PORT, clientSendPort);
    }

    public static void setClientSendPort(int port) {
        LKVMgr.getInstance().putInt(DeviceKey.KEY_CLIENT_SEND_PORT, port);
    }


}
