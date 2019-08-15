package com.zing.netty.d006_scheduled_heartbeat_with_reconnect.client;

import com.zing.netty.dto.Dto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * create at     2019-08-13 14:50
 *
 * @author zing
 * @version 0.0.1
 */
public class HeartBeatReconnectClientInitializer extends ChannelInitializer<SocketChannel> {

    private String clientName;
    private HeartBeatWithReconnectConnector connector;

    public HeartBeatReconnectClientInitializer(String clientName, HeartBeatWithReconnectConnector connector) {
        this.clientName = clientName;
        this.connector = connector;

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new IdleStateHandler(2, 2, 2, TimeUnit.SECONDS))
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(Dto.ZingMessage.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new ScheduledHeartBeatHandler(clientName, connector));
    }
}
