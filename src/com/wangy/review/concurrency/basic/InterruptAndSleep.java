package com.wangy.review.concurrency.basic;

import java.util.concurrent.TimeUnit;

/**
 * sleep 休眠可以被阻塞
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/11 / 14:44
 */
public class InterruptAndSleep {
    static Thread apple = new Thread(new InterruptSleepOn(), "apple");

    public static void main(String[] args) {
//        sleepBeforeInterrupt();
        sleepAfterInterrupt();
    }

    // 先中断线程
    static void sleepBeforeInterrupt() {
        apple.start();
        // 调用start()方法之后，线程的状态即为RUNNABLE
        System.out.println(apple.getState());
        apple.interrupt();
    }

    /**
     * 先使线程休眠
     * <p>
     * 此方法运行有2种可能的情况：
     * <pre><code>
     * id(0 apple)
     * TIMED_WAITING
     * id(0 apple) is interrupted
     * id(0 apple)
     * id(0 apple)
     * </code></pre>
     * <pre><code>
     * id(0 apple)
     * id(0 apple)
     * RUNNABLE
     * id(0 apple) is interrupted
     * id(0 apple)
     * </code></pre>
     * 可以看到，2种情况输出的线程状态不同，情况1是期望的结果，也是大概率出现的结果，但是情况2还是有出现的可能<br>
     * 实际上main线程休眠结束之后和结束休眠的apple线程还是出现了抢占cpu资源的情形
     */

    static void sleepAfterInterrupt() {
        apple.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(apple.getState());
        // 此时调用中断，线程的状态是TIMED_WAITING
        apple.interrupt();
    }

    static class InterruptSleep implements Runnable {
        protected static int count = 0;
        protected final int id = count++;
        protected int countDown = 3;

        public InterruptSleep() {
        }

        public void info() {
            System.out.println("id(" + id + " " + Thread.currentThread().getName() + ") ");
        }

        @Override
        public void run() {
            try {
                while (countDown-- > 0) {
                    info();
                    // Thread.sleep(100);
                    // Java SE5 or later style
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                // 捕获异常时线程run()方法中止
            } catch (InterruptedException e) {
                System.out.println("id(" + id + " " + Thread.currentThread().getName() + ") is" + " interrupted");
            }
        }
    }

    static class InterruptSleepOn extends InterruptSleep {
        @Override
        public void run() {
            while (countDown-- > 0) {
                try {
                    info();
                    TimeUnit.MILLISECONDS.sleep(100);
                    // 捕获异常后继续执行run()方法
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                    System.out.println("id(" + id + " " + Thread.currentThread().getName() + ") is" + " interrupted");
                }
            }
        }
    }
}
