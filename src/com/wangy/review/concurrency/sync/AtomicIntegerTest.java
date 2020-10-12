package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用原子类<del>实现无锁同步</del>
 * <br>
 *     并没有实现同步，任意时刻main线程通过{@link AtomicIntegerTest#getValue()}获取到的值并不一定是期望的最新值
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/15 / 18:12
 * @see AtomicTest
 */
public class AtomicIntegerTest implements Runnable {

    private AtomicInteger i = new AtomicInteger(0);

    public int getValue() {
        // atomic operation
        return i.get();
    }

    /**
     * 无锁的原因不是因为原子性，而是因为有且只有一个原子操作
     * 若此处使用
     * <pre>
     *     i.incrementAndGet();
     *     i.incrementAndGet();
     * </pre>
     * 那么依旧和{@link AtomicTest}一样失败
     */
    private void increment() {
        i.addAndGet(2);
//        i.incrementAndGet();
//        i.incrementAndGet();
    }

    @Override
    public void run() {
        while (true) increment();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerTest act = new AtomicIntegerTest();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(act);
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        // 5s后结束程序
        s.schedule(() -> {
            System.out.println("Aborting");
            executor.shutdown();
            s.shutdown();
            System.exit(0);
        }, 5, TimeUnit.SECONDS);

        while (true) {
            int value = act.getValue();
            if (value % 2 != 0) {
                System.out.println(value);
                System.exit(0);
            }
        }
    }
}
