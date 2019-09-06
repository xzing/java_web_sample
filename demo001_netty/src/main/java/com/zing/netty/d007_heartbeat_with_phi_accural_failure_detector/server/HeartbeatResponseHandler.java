package com.zing.netty.d007_heartbeat_with_phi_accural_failure_detector.server;

import com.zing.netty.dto.Dto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * create at     2019-08-16 11:16
 *
 * @author zing
 * @version 0.0.1
 */
public class HeartbeatResponseHandler extends SimpleChannelInboundHandler<Dto.ZingMessage> {

    /**
     * 用于超时模拟
     */
    ConcurrentHashMap<String, Integer> timeCatch = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Dto.ZingMessage msg) throws Exception {

    }
}
