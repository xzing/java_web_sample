package com.zing.netty.d004_client_reconnect_sample.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import static com.zing.netty.d006.reconnect.server.PongMessageHandler.PING;

/**
 * create at     2019-08-12 10:53
 *
 * @author zing
 * @version 0.0.1
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    System.out.println("READER_IDLE");
                    break;
                case WRITER_IDLE:
                    System.out.println("WRITER_IDLE");
                    break;
                case ALL_IDLE:
                    System.out.println("ALL_IDLE");
                    ctx.channel().writeAndFlush(PING);
                    System.out.println("send PING");
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(">>> Need Reconnect!!");
        // super.channelInactive(ctx);
    }
}
