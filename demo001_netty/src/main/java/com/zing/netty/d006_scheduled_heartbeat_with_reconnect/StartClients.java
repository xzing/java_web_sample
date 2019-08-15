package com.zing.netty.d006_scheduled_heartbeat_with_reconnect;

import com.zing.netty.d006_scheduled_heartbeat_with_reconnect.client.HeartBeatWithReconnectConnector;

/**
 * create at     2019-08-15 11:10
 *
 * @author zing
 * @version 0.0.1
 */
public class StartClients {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            HeartBeatWithReconnectConnector client = new HeartBeatWithReconnectConnector();
            client.linkStart(8800, "0001");
        });
        Thread t2 = new Thread(() -> {
            HeartBeatWithReconnectConnector client = new HeartBeatWithReconnectConnector();
            client.linkStart(8800, "0002");
        });

        Thread t3 = new Thread(() -> {
            HeartBeatWithReconnectConnector client = new HeartBeatWithReconnectConnector();
            client.linkStart(8800, "0003");
        });

        t1.start();
        t2.start();
        t3.start();


    }

}
