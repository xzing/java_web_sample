package com.zing.netty.d006_scheduled_heartbeat_with_reconnect.server;

import com.zing.netty.dto.Dto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

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
    ConcurrentHashMap<String, Integer> timeCatch = new ConcurrentHashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String name = ctx.channel().id().asShortText();
        timeCatch.put(name, 1);
        log.info("channel {} active", name);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Dto.ZingMessage zingMessage) throws Exception {
        String name = zingMessage.getHeartBeatMap().get("name");
        String ping = zingMessage.getHeartBeatMap().get("ping");
        int i = timeCatch.get(channelHandlerContext.channel().id().asShortText());
        long sleepTime = i * 1000;
        log.info("name:{}; ping:{}; sleep {} mills", name, ping, sleepTime);
        Thread.sleep(sleepTime);
        Dto.ZingMessage rpl = Dto.ZingMessage.newBuilder()
                .putHeartBeat("name", name)
                .putHeartBeat("pong", ping)
                .build();
        channelHandlerContext.writeAndFlush(rpl);
        timeCatch.put(channelHandlerContext.channel().id().asShortText(), 1 + i);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String name = ctx.channel().id().asShortText();
        timeCatch.remove(name);
        log.info("channel {} inactive", name);
    }
}
