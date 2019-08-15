package com.zing.netty.d005_scheduled_heart_beat_sample;

import com.zing.netty.d005_scheduled_heart_beat_sample.client.HeartBeatClient;
import com.zing.netty.d005_scheduled_heart_beat_sample.server.HeartBeatServer;

/**
 * create at     2019-08-13 11:49
 *
 * @author zing
 * @version 0.0.1
 */
public class Starter {
    public static void main(String[] args) {
        Thread ts = new Thread(() -> {
            HeartBeatServer server = new HeartBeatServer();
            server.start(8800);
        });

        ts.start();


        Thread t1 = new Thread(() -> {
            HeartBeatClient client = new HeartBeatClient();
            client.linkStart(8800,"0001");
        });
        Thread t2 = new Thread(() -> {
            HeartBeatClient client = new HeartBeatClient();
            client.linkStart(8800,"0002");
        });
        Thread t3 = new Thread(() -> {
            HeartBeatClient client = new HeartBeatClient();
            client.linkStart(8800,"0003");
        });
        Thread t4 = new Thread(() -> {
            HeartBeatClient client = new HeartBeatClient();
            client.linkStart(8800,"0004");
        });
        Thread t5 = new Thread(() -> {
            HeartBeatClient client = new HeartBeatClient();
            client.linkStart(8800,"0005");
        });

        t1.start();
        t2.start();
        t3.start();
        // t4.start();
        // t5.start();


    }
}
