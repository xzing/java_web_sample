package com.zing.netty.d004_client_reconnect_sample.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

import static com.zing.netty.d004_client_reconnect_sample.server.PongMessageHandler.PING;
import static com.zing.netty.d004_client_reconnect_sample.server.PongMessageHandler.PONG;


/**
 * create at     2019-08-12 11:30
 *
 * @author zing
 * @version 0.0.1
 */
public class PingMessageHandler extends SimpleChannelInboundHandler<String> {
    private Client connector;

    public PingMessageHandler(Client connector) {
        this.connector = connector;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (PONG.trim().equals(msg.trim())) {
            System.out.println("get pong");
            Thread.sleep(3000);
            ctx.writeAndFlush(PING);
            System.out.println("send ping");
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(PING);
        System.out.println("send ping");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (cause instanceof IOException) {
            ctx.close();
            connector.doConnect();
        }
    }
}
