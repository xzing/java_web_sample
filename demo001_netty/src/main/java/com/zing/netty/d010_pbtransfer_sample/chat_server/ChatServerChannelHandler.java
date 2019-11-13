package com.zing.netty.d010_pbtransfer_sample.chat_server;

import com.zing.netty.dto.Dto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zing
 * @version 0.0.1
 * @date 2019-07-06 16:15
 */
@Slf4j
public class ChatServerChannelHandler extends SimpleChannelInboundHandler<Dto.DemoMessage2> {


    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Dto.DemoMessage2 msg) throws Exception {
        Channel channel = ctx.channel();
        log.info("read:{}", msg);
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[" + channel.remoteAddress() + "]: " + msg + "\n");
            } else {
                ch.writeAndFlush("[self]: " + msg + "\n");
            }
        });

    }

}
