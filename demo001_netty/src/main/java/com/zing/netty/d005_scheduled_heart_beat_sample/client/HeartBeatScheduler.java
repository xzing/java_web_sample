package com.zing.netty.d005_scheduled_heart_beat_sample.client;

import java.util.concurrent.*;

/**
 * create at     2019-08-13 14:56
 *
 * @author zing
 * @version 0.0.1
 */
public class HeartBeatScheduler {
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    /**
     * 立即执行
     *
     * @param r
     */
    public static void execute(Runnable r) {
        scheduler.execute(r);
    }


    /**
     * 延时执行
     *
     * @param r
     */
    public static ScheduledFuture execute(Runnable r, long delay, TimeUnit timeUnit) {
        return scheduler.schedule(r, delay, timeUnit);
    }

    /**
     * 延时执行
     *
     * @param r
     */
    public static ScheduledFuture execute(Callable r, long delay, TimeUnit timeUnit) {
        return scheduler.schedule(r, delay, timeUnit);
    }

    /**
     * 延时循环执行
     *
     * @param r
     */
    public static ScheduledFuture repeat(Runnable r, long delay, TimeUnit timeUnit) {
        return scheduler.scheduleWithFixedDelay(r, 0, delay, timeUnit);
    }


}
