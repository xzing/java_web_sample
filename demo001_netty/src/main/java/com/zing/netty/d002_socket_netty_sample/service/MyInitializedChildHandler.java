package com.zing.netty.d002_socket_netty_sample.service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 *
 * @author zing
 * @version 0.0.1
 * @date 2019-06-27 23:02
 */
public class MyInitializedChildHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // HTTP 的处理
        // ch.pipeline()
        //         .addLast("HttpServerCodec", new HttpServerCodec())
        //         .addLast("MyHttpResponse",new MyHttpResponseChannel());

        ch.pipeline()
                .addLast("ServerLengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                .addLast("ServerLengthFieldPrepender", new LengthFieldPrepender(4))
                .addLast("ServerStringDecoder", new StringDecoder(CharsetUtil.UTF_8))
                .addLast("ServerStringEncoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("MySocketServerHandler", new MySocketServerHandler());
    }
}
