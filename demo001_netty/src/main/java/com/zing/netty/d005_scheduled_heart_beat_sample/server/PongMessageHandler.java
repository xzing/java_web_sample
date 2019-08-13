package com.zing.netty.d005_scheduled_heart_beat_sample.server;

import com.zing.netty.dto.Dto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * create at     2019-08-13 14:29
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class PongMessageHandler extends SimpleChannelInboundHandler<Dto.ZingMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Dto.ZingMessage zingMessage) throws Exception {
        UUID uuid = UUID.randomUUID();
        String address = channelHandlerContext.channel().remoteAddress().toString();
        log.info("HB {}:[{}]", address, zingMessage.getHeartBeatMap());
        Dto.ZingMessage msg = Dto.ZingMessage.newBuilder()
                .putHeartBeat("ZingPong>>>", uuid.toString())
                .build();
        channelHandlerContext.writeAndFlush(msg);
    }


}
