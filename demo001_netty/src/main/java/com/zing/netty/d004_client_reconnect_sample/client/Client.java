package com.zing.netty.d004_client_reconnect_sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * create at     2019-08-12 11:07
 *
 * @author zing
 * @version 0.0.1
 */
public class Client {
    static EventLoopGroup nioGroup = new NioEventLoopGroup();
    static Bootstrap bootstrap;

    public static void main(String[] args) throws InterruptedException {
        bootstrap = new Bootstrap();
        bootstrap.group(nioGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChildInitializeHandler());

        try {
            doConnect();

            while (isNotStop) {
                Thread.sleep(10000);
            }
        } finally {
            nioGroup.shutdownGracefully();
        }

    }

    public static final boolean isNotStop = true;

    private static void doConnect() {
        try {
            ChannelFuture cf = bootstrap.connect("localhost", 8080)
                    .addListener((ChannelFuture f) -> {
                        if (!f.isSuccess()) {
                            System.out.println("reconnect");
                            f.channel().eventLoop().schedule(() -> doConnect(), 5, TimeUnit.SECONDS);
                        } else {
                            System.out.println("stop reconnect");
                        }
                    })
                    .sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
