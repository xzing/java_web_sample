package com.zing.netty.d006_scheduled_heartbeat_with_reconnect.client;

import com.zing.netty.dto.Dto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.handler.timeout.IdleState.READER_IDLE;

/**
 * create at     2019-08-13 14:53
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class ScheduledHeartBeatHandler extends SimpleChannelInboundHandler<Dto.ZingMessage> {
    AtomicInteger time = new AtomicInteger(3);

    AtomicBoolean running = new AtomicBoolean(false);

    private String name;

    private HeartBeatWithReconnectConnector connector;

    public ScheduledHeartBeatHandler(String name, HeartBeatWithReconnectConnector connector) {
        this.connector = connector;
        this.name = name;

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (READER_IDLE.equals(state)) {
                log.info("{} read idle! count:{}", name, 3 - time.get());
                int t = time.decrementAndGet();
                if (t < 0) {
                    // 停止心跳
                    running.compareAndSet(true, false);
                    // 移除定时任务
                    SchedulerCanBeStop.cancelHeartbeat(name);
                    // 关闭连接
                    ctx.close();
                }
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Dto.ZingMessage zingMessage) throws Exception {
        resetRetryLimit();
        log.info("response:{}", zingMessage.getHeartBeatMap());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        resetRetryLimit();
        log.info("link active,start heart beat");
        running.compareAndSet(false, true);
        Runnable r = () -> {
            log.info("{} send ping", name);
            UUID uuid = UUID.randomUUID();
            Dto.ZingMessage msg = Dto.ZingMessage.newBuilder()
                    .putHeartBeat("name", name)
                    .putHeartBeat("ping", uuid.toString())
                    .build();
            ctx.writeAndFlush(msg);
        };
        SchedulerCanBeStop.addClientHeartTask(name, r);

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        SchedulerCanBeStop.cancelHeartbeat(name);
        running.compareAndSet(true, false);
        super.handlerRemoved(ctx);
        connector.stopClient();
    }

    private void resetRetryLimit() {
        time.set(3);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            ctx.close();
            connector.doConnect();
            log.warn("IOException:", cause);
        }
    }
}
