package com.zing.netty.d005_scheduled_heart_beat_sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * create at     2019-08-13 14:46
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class HeartBeatClient {
    EventLoopGroup clientGroup = new NioEventLoopGroup();
    Bootstrap client = new Bootstrap();
    private volatile boolean notStop = true;

    public void linkStart(int port) {
        client.group(clientGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new HeartBeatClientInitializer());
        if (port < 1000 || port > 65535) {
            port = 8800;
        }
        log.info("client start, port :[{}]", port);
        try {
            ChannelFuture future = client.connect("localhost", port).sync();
            while (notStop) {
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }

    }


}
