package com.zing.netty.d008_channel_pool_sample.chat_client;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * @author zing
 * @version 0.0.1
 * @date 2019-07-06 16:36
 */
public class ChatClient {

    public static void main(String[] args) {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChatClientInitializer())
                .remoteAddress("localhost", 8899);


        ChannelPool pool = new FixedChannelPool(bootstrap,
                new ChannelPoolHandler() {
                    @Override
                    public void channelReleased(Channel ch) throws Exception {
                        ch.writeAndFlush(Unpooled.EMPTY_BUFFER);
                    }

                    @Override
                    public void channelAcquired(Channel ch) throws Exception {

                    }

                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        NioSocketChannel channel = (NioSocketChannel) ch;
                        channel.pipeline().addLast(new ChatClientInitializer());
                    }
                }, 10);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                Channel ch = pool.acquire().get();
                ch.writeAndFlush(reader.readLine() + "\r\n");
                pool.release(ch);
                Thread.sleep(200);
            }
        } catch (InterruptedException | IOException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }


    }
}
