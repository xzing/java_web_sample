package com.zing.netty.d002_socket_netty_sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 * @author zing
 * @version 0.0.1
 * @date 2019-07-02 11:03
 */
public class MyNettyClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        Bootstrap client = new Bootstrap();
        try {
            client.group(clientGroup);
            client.channel(NioSocketChannel.class)
                    .handler(new MyClientInitializedHandler());
            ChannelFuture future = client.connect("localhost", 8899).sync();
            future.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}
