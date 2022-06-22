package com.wangy.review.concurrency.basic;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 线程本地变量的简单使用
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/17 / 15:14
 */
public class ThreadLocalVariableHolder {
    // Java 8 提供的方法
    private static final ThreadLocal<Integer> value = ThreadLocal.withInitial(() -> {
        Random r = new Random();
        return r.nextInt(10);
    });

    static class Task implements Runnable {

        static void increment() {
            value.set(value.get() + 1);
        }

        static Integer getValue() {
            return value.get();
        }

        @Override
        public String toString() {
            return Thread.currentThread() + ": " + getValue();
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                increment();
                System.out.println(this);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            new Thread(new Task()).start();
        }
        TimeUnit.MILLISECONDS.sleep(1);
        System.exit(0);
    }
}
