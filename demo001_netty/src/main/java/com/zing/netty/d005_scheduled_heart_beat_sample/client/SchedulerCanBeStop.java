package com.zing.netty.d005_scheduled_heart_beat_sample.client;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * create at     2019-08-13 14:56
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class SchedulerCanBeStop {
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public static AtomicBoolean isRunning = new AtomicBoolean(false);

    private static ConcurrentHashMap<String, Runnable> tasks = new ConcurrentHashMap<>();


    /**
     * 开始心跳
     *
     * @param name
     * @param r
     */
    public static void addClientHeartTask(String name, Runnable r) {
        tasks.put(name, r);
        startHeartbeat();
    }

    /**
     * 停止心跳
     *
     * @param name
     */
    public static void cancelHeartbeat(String name) {
        if (isRunning.get()) {
            tasks.remove(name);
            log.info("stop task:{}", name);
            if (tasks.isEmpty()) {
                scheduler.shutdown();
                isRunning.set(false);
            }
        }
    }

    private static void startHeartbeat() {
        if (isRunning.compareAndSet(false, true)) {
            if (scheduler == null || scheduler.isTerminated() || scheduler.isShutdown()) {
                scheduler = Executors.newScheduledThreadPool(4);
            }

            Runnable r = () -> tasks.values().forEach(Runnable::run);
            // FIXME 改为可配置心跳时间
            repeat(r, 10, TimeUnit.SECONDS);
        }
    }

    /**
     * 延时循环执行
     */
    public static ScheduledFuture repeat(Runnable r, long delay, TimeUnit timeUnit) {
        return scheduler.scheduleWithFixedDelay(r, 0, delay, timeUnit);
    }


}
