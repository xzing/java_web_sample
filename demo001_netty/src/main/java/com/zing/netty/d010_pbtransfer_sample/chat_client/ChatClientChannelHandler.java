package com.zing.netty.d010_pbtransfer_sample.chat_client;

import com.zing.netty.dto.Fuck;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * create at     2019-07-06 16:48
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class ChatClientChannelHandler extends SimpleChannelInboundHandler<Fuck.DemoMessage1> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Fuck.DemoMessage1 msg) throws Exception {
        log.info("response:{}", msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        log.info("online");
    }
}
