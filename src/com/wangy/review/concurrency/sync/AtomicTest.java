package com.wangy.review.concurrency.sync;

import java.util.concurrent.TimeUnit;

/**
 * 不要使用原子性代替同步！
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/15 / 17:15
 * @see SynchronizedEvenGeneratorWithIntrinsicLock
 */
public class AtomicTest implements Runnable {
    private int i = 0;

    public int getValue() {
        // atomic operation
        return i;
    }

    private void increment() {
//        i++;
//        i++;
        // 将上述操作换做
        i += 2;
        // 即是安全的
    }

    @Override
    public void run() {
        while (true) increment();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicTest at = new AtomicTest();
        new Thread(at).start();
        TimeUnit.MILLISECONDS.sleep(1);
        while (true) {
            // the value can still be odd
            int value = at.getValue();
            if (value % 2 != 0) {
                System.out.println(value);
                System.exit(0);
            }
        }
    }
}
