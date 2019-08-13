package com.zing.netty.d005_scheduled_heart_beat_sample.client;

import com.zing.netty.dto.Dto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * create at     2019-08-13 14:53
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class ScheduledHeartBeatHandler extends SimpleChannelInboundHandler<Dto.ZingMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Dto.ZingMessage zingMessage) throws Exception {
        log.info("zing:{}", zingMessage.getHeartBeatMap());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("link active,start heart beat");
        HeartBeatScheduler.repeat(() -> {
            UUID uuid = UUID.randomUUID();
            Dto.ZingMessage msg = Dto.ZingMessage.newBuilder().putHeartBeat("Ping>>>", uuid.toString()).build();
            ctx.writeAndFlush(msg);
        }, 10, TimeUnit.SECONDS);

    }
}
