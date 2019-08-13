package com.zing.netty.d004_client_reconnect_sample.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * create at     2019-08-12 10:58
 *
 * @author zing
 * @version 0.0.1
 */
public class PongMessageHandler extends SimpleChannelInboundHandler<String> {
    public static final String PING = "ping\r\n";
    public static final String PONG = "pong\r\n";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("message is:" + msg);
        if (PING.trim().equals(msg.trim())) {
            System.out.println("get ping");
            ctx.writeAndFlush(PONG);
            System.out.println("send pone");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress().toString() + "on line");
    }
}
