package com.zing.netty.d002_socket_netty_sample.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zing
 * @version 0.0.1
 * @date 2019-07-02 11:15
 */
@Slf4j
public class MyClientInitializedHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast("ClientLengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                .addLast("ClientLengthFieldPrepender", new LengthFieldPrepender(4))
                .addLast("ClientStringDecoder", new StringDecoder(CharsetUtil.UTF_8))
                .addLast("ServerStringEncoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("MyClientProcessHandler", new MyClientChannelHandler());

    }
}
