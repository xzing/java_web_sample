package com.zing.netty.d006_scheduled_heartbeat_with_reconnect;

import com.zing.netty.d006_scheduled_heartbeat_with_reconnect.server.HeartBeatServer;

/**
 * create at     2019-08-15 10:59
 *
 * @author zing
 * @version 0.0.1
 */
public class StartServer {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            HeartBeatServer server = new HeartBeatServer();
            server.start(8800);
        });
        t.start();
    }
}
