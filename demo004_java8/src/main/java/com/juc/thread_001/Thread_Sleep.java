package com.juc.thread_001;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Is Zing  Created At 2021/6/7
 */
public class Thread_Sleep {
    public static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread ta = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("ta in sleep!");
                Thread.sleep(5000);
                System.out.println("ta is awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        Thread tb = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("tb in sleep!");
                Thread.sleep(5000);
                System.out.println("tb is awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        ta.start();
        tb.start();
    }
}
