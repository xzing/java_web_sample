package com.zing.netty.d004_client_reconnect_sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * create at     2019-08-12 11:07
 *
 * @author zing
 * @version 0.0.1
 */
public class Client {
    private EventLoopGroup nioGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    private volatile boolean isNotStop = true;
    public AtomicInteger reconnectLimit = new AtomicInteger(3);


    public static void main(String[] args) throws InterruptedException {
        Client c = new Client();
        c.start();


    }

    public void start() throws InterruptedException {
        bootstrap = new Bootstrap();
        bootstrap.group(nioGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChildInitializeHandler(this));

        try {
            ChannelFuture future = doConnect();

            while (isNotStop) {
                Thread.sleep(10000);
            }

            if (future != null) {
                future.channel().closeFuture().sync();
            }


        } finally {
            nioGroup.shutdownGracefully();
        }
    }


    public ChannelFuture doConnect() {
        ChannelFuture cf;
        try {
            cf = bootstrap.connect("localhost", 8080)
                    .addListener((ChannelFuture f) -> {
                        if (!f.isSuccess()) {
                            int times = reconnectLimit.decrementAndGet();
                            if (times < 0) {
                                isNotStop = false;
                            }
                            System.out.println("reconnect");
                            f.channel().eventLoop().schedule(() -> doConnect(), 5, TimeUnit.SECONDS);
                        } else {
                            reconnectLimit.set(3);
                            System.out.println("stop reconnect");
                        }
                    })
                    .sync();

        } catch (Exception e) {
            e.printStackTrace();
            cf = null;
        }
        return cf;
    }
}
