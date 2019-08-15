package com.zing.netty.d006_scheduled_heartbeat_with_reconnect.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * create at     2019-08-13 14:46
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class HeartBeatWithReconnectConnector {
    EventLoopGroup clientGroup = new NioEventLoopGroup();
    Bootstrap client = new Bootstrap();
    private volatile boolean notStop = true;
    private int port;

    /**
     *
     */
    AtomicInteger retryLimit = new AtomicInteger();

    public void linkStart(int port, String name) {
        this.port = port;
        resetRetryLimit();
        client.channel(NioSocketChannel.class)
                .group(clientGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new HeartBeatReconnectClientInitializer(name, this));
        if (port < 1000 || port > 65535) {
            this.port = 8800;
        }
        try {
            ChannelFuture channelFuture = doConnect();
            while (notStop) {
                Thread.sleep(10000);
            }
            if (channelFuture != null) {
                channelFuture.channel().closeFuture().sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }

    }

    public ChannelFuture doConnect() {
        ChannelFuture future;
        log.info("client start, port :[{}]", port);
        future = client.connect("localhost", port)
                .addListener((ChannelFuture f) -> {
                    if (!f.isSuccess()) {
                        System.out.println("reconnect");
                        int retryTimes = retryLimit.decrementAndGet();
                        if (retryTimes < 0) {
                            stopClient();
                        }
                        f.channel().eventLoop().schedule(() -> doConnect(), 5, TimeUnit.SECONDS);
                    } else {
                        resetRetryLimit();
                        System.out.println("connected");
                    }
                });

        return future;
    }

    private void resetRetryLimit() {
        retryLimit.set(4);
    }

    public void stopClient() {
        log.info("Client Stop");
        notStop = false;
    }


}
