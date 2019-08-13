package com.zing.netty.d001_first_http_netty_program.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author zing
 * @version 0.0.1
 * @date 2019-06-24 20:50
 */
public class MyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channelInactive -- ");
    }

    // @Override
    // protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    //     System.out.println(ctx.channel().remoteAddress() + "-----" + msg);
    //     ctx.channel().writeAndFlush(msg);
    // }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        ByteBuf content = Unpooled.copiedBuffer("HelloNetty", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");

        ctx.writeAndFlush(response);
    }
}
