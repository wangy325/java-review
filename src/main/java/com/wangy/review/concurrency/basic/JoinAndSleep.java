package com.wangy.review.concurrency.basic;

import java.util.concurrent.TimeUnit;

/**
 * join也可以被中断
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 01:12
 */
public class JoinAndSleep {
    public static void main(String[] args) {
        Sleeper s1 = new Sleeper("s1", 200);
        Joiner j1 = new Joiner("j1", s1);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 此时j1的状态是WAITING
        System.out.println(j1.getState());

        s1.interrupt();
//        j1.interrupt();
    }

    static class Sleeper extends Thread {
        private final int duration;

        public Sleeper(String name, int duration) {
            super(name);
            this.duration = duration;
            // 在构造器中直接启动线程，这种情形称为自管理线程
            start();
        }

        @Override
        public void run() {
            try {
                sleep(duration);
            } catch (InterruptedException e) {
                // 此处的中断状态是false 因为sleep()方法抛出此异常之后线程的中断状态被清除
                System.out.println(currentThread() + "is interrupted ? " + isInterrupted());
                return;
            }
            System.out.println(currentThread() + " has awakened.");
        }
    }

    static class Joiner extends Thread {
        private final Sleeper sleeper;

        public Joiner(String name, Sleeper sleeper) {
            super(name);
            this.sleeper = sleeper;
            start();
        }

        @Override
        public void run() {
            try {
                // 等待sleeper完成
                sleeper.join();
            } catch (InterruptedException e) {
                // 在当前线程挂起等待sleeper线程完成的过程中，若调用interrupt()方法中断线程则会抛出此异常
                System.out.println(currentThread() + " interrupted.");
                return;
            }
            System.out.println(currentThread() + "join completed.");
        }
    }
}
