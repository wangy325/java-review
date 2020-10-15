package com.wangy.review.concurrency.sync;

import lombok.val;

import java.util.concurrent.TimeUnit;

/**
 * 不能过分信任原子性，更不要使用原子性代替同步！
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/15 / 17:15
 * @see EvenGeneratorWithAdhocLock
 */
public class AtomicTest implements Runnable {
    private volatile int i = 0;

    public int getValue() {
        // 虽然这个方法只有一句原子性操作
        // 但如果不使用同步，main方法依然会出现讹误
        return i;
    }

    private synchronized void increment() {
        i++;
        i++;
    }

    @Override
    public void run() {
        while (true) increment();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("PRESS CTRL-C to EXIT");
        AtomicTest at = new AtomicTest();
        new Thread(at).start();
        TimeUnit.MILLISECONDS.sleep(1);
        while (true) {
            int value = at.getValue();
            if (value % 2 != 0) {
                System.out.println(value);
                System.exit(0);
            }
        }
    }
}
