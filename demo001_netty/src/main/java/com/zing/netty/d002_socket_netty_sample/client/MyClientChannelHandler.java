package com.zing.netty.d002_socket_netty_sample.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author zing
 * @version 0.0.1
 * @date 2019-07-04 14:36
 */
@Slf4j
public class MyClientChannelHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("ClientChannelRead: {}", msg);
        log.info("Address: {}", ctx.channel().remoteAddress());
        ctx.channel().writeAndFlush("Hello From Client" + UUID.randomUUID());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("HI!");
        super.channelActive(ctx);
    }
}
