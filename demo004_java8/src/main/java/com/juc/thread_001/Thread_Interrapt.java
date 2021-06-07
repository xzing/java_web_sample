package com.juc.thread_001;

/**
 * @author Is Zing  Created At 2021/6/7
 */
public class Thread_Interrapt {
    public static void main(String[] args) throws InterruptedException {

        // new Thread_Interrapt().testInterrupt1();

        // new Thread_Interrapt().testInterrupt2();
        new Thread_Interrapt().testInterrupt3();


    }

    private void testInterrupt3() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().interrupted()) {
            }
            Thread currentThead = Thread.currentThread();
            System.out.println("thread is interrupted:" + currentThead.isInterrupted());
        });

        thread.start();

        thread.interrupt();

        System.out.println("isInterruptA:" + thread.isInterrupted());
        System.out.println("isInterruptB:" + thread.interrupted());
        System.out.println("isInterruptC:" + Thread.interrupted());
        System.out.println("isInterruptD:" + thread.isInterrupted());
        thread.join();

        System.out.println("main is over");

    }

    private void testInterrupt2() throws InterruptedException {

        Thread thread_1 = new Thread(() -> {
            try {
                System.out.println("thread begin for 200 second!");
                Thread.sleep(200000);
                System.out.println("thread end for 200 second!");
            } catch (InterruptedException e) {
                System.out.println(" thread_1 is interrupted while sleeping ");
                return;
            }
            System.out.println("thread_1 leaving normally");
        });

        // 启动
        thread_1.start();
        // 确保Thread_1 进入sleep状态
        Thread.sleep(1000);
        // 打断子线程的休眠。子线程从sleep返回
        thread_1.interrupt();
        // 等待子线程执行完毕
        thread_1.join();

        System.out.println("main thread is over!");

    }

    private void testInterrupt1() throws InterruptedException {

        Thread ta = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread() + " hello");
            }
        });
        ta.start();
        Thread.sleep(100);
        System.out.println("main thread interrupt ta");
        ta.interrupt();
        ta.join();
        System.out.println("main over");
    }
}
