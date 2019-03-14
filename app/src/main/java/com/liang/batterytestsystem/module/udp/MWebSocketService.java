package com.liang.batterytestsystem.module.udp;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author : Amarao
 * CreateAt : 11:12 2019/3/14
 * Describe :
 */
public class MWebSocketService extends WebSocketServer {
    public MWebSocketService(int port) {
        super(new InetSocketAddress(port));
    }

    public MWebSocketService(InetSocketAddress address) {
        super(address);
    }

    private static void print(String msg) {
        System.out.println(String.format("[%d] %s", System.currentTimeMillis(), msg));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
        String message = String.format("(%s) <进入房间！>", address);
        sendToAll(message);
        System.out.println(message);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
        String message = String.format("(%s) <退出房间！>", address);
        sendToAll(message);

        System.out.println(message);

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        //服务端接收到消息
        String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
        String message = String.format("(%s) %s", address, s);
        //将消息发送给所有客户端
        sendToAll(message);
        System.out.println(message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        if (null != webSocket) {
            webSocket.close(0);
        }
        e.printStackTrace();
    }

    @Override
    public void onStart() {

    }

    public void sendToAll(String message) {
        // 获取所有连接的客户端
        Collection<WebSocket> connections = this.getConnections();
        //将消息发送给每一个客户端
        for (WebSocket client : connections) {
            client.send(message);
        }
    }
}