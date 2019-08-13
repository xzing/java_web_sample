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


        Runnable r = () -> {
            HeartBeatClient client = new HeartBeatClient();
            client.linkStart(8800);
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        ts.start();
        t1.start();
        t2.start();


    }
}
