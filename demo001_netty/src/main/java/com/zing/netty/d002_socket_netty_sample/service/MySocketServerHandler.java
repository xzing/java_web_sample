package com.zing.netty.d002_socket_netty_sample.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author zing
 * @version 0.0.1
 * @date 2019-07-02 11:48
 */
@Slf4j
public class MySocketServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("get Request From:{}", ctx.channel().remoteAddress());
        log.info("get Request message:{}", msg);
        ctx.channel().writeAndFlush("Server Response: " + UUID.randomUUID());
    }
}
