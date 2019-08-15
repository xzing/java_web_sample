package com.zing.netty.d005_scheduled_heart_beat_sample.server;

import com.zing.netty.dto.Dto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * create at     2019-08-13 14:29
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class PongMessageHandler extends SimpleChannelInboundHandler<Dto.ZingMessage> {

    /**
     * 用于超时模拟
     */
    AtomicInteger t = new AtomicInteger(1);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Dto.ZingMessage zingMessage) throws Exception {
        String address = channelHandlerContext.channel().remoteAddress().toString();
        Map<String, String> msg = zingMessage.getHeartBeatMap();
        String name = msg.get("name");
        String ping = msg.get("ping");
        long i = t.getAndIncrement() * 1000;

        Thread.sleep(i);
        log.info("from:{};sleep {} mills; ping:{}; address:{} ", name, i, ping, address);
        Dto.ZingMessage rep = Dto.ZingMessage.newBuilder()
                .putHeartBeat("name",name)
                .putHeartBeat("pong", ping)
                .build();
        channelHandlerContext.writeAndFlush(rep);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("{} active", ctx.channel().id().asShortText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("{} inactive", ctx.channel().id().asShortText());

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        log.info("{} added", ctx.channel().id().asShortText());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        log.info("{} removed", ctx.channel().id().asShortText());

    }
}
