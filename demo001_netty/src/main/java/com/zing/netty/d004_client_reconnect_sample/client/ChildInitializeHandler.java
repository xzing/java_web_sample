package com.zing.netty.d004_client_reconnect_sample.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * create at     2019-08-12 11:11
 *
 * @author zing
 * @version 0.0.1
 */
public class ChildInitializeHandler extends ChannelInitializer<SocketChannel> {

    private Client connector;

    public ChildInitializeHandler(Client connector) {
        this.connector = connector;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new IdleStateHandler(5, 7, 10, TimeUnit.SECONDS))
                .addLast(new HeartBeatHandler())
                .addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                .addLast(new PingMessageHandler(connector))
        ;
    }
}
