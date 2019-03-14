package com.liang.batterytestsystem.module.udp;

import com.liang.liangutils.utils.LLogX;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * @author : Amarao
 * CreateAt : 11:04 2019/3/14
 * Describe :
 */
public class ChatServer extends WebSocketServer {


    public ChatServer(InetSocketAddress address, int decoders) {
        super(address, decoders);
    }

    public ChatServer(InetSocketAddress address) {
        super(address);
    }

    public ChatServer(int port) {
        this(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        showInfo("开始");
    }

    @Override
    public void onMessage(WebSocket conn, String msg) {
        final String info = conn.getRemoteSocketAddress().getAddress().getHostAddress() + ", msg=>" + msg;
        showInfo("读取：" + info);
        send2All(info);
        showInfo("发送：" + info);
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        final String info = conn.getRemoteSocketAddress().getAddress().getHostAddress() + "接入!";
        showInfo(info);

        send2All(info);
        showInfo("发送：" + info);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        final String info = conn.getRemoteSocketAddress().getAddress().getHostAddress() + " 断开!，reason=" + reason;
        showInfo(info);

        send2All(info);
        showInfo("发送：" + info);
    }


    @Override
    public void onError(WebSocket conn, Exception e) {

        String info = conn.getRemoteSocketAddress().getAddress().getHostAddress() + ", error=>" + e;
        showInfo(info);
    }


    /**
     * @param info
     */
    public void send2All(String info) {
        Iterator<WebSocket> interator = getConnections().iterator();
        while (interator.hasNext()) {
            interator.next().send(info);
        }
    }

    public void showInfo(String info) {
        LLogX.e("服务端：" + info);
    }


}
