package com.wangy.review.concurrency.sync;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用原子类实现无锁同步
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/15 / 18:12
 * @see AtomicTest
 */
public class AtomicClassTest implements Runnable {
    /**
     * 使用volatile使i在读写线程之前保持可见，试图实现iSet集中没有重复元素的目的
     * 还是可能会失败，这不是原子性与可见性的问题，而是线程的调度问题，读线程可能连续读两次
     * 要解决问题，需要实现"读-写-读-写"的线程调度
     */
    private volatile AtomicInteger i = new AtomicInteger(0);
    private Set<Integer> iSet = Collections.synchronizedSet(new TreeSet<>());

    public boolean add(int i) {
        return iSet.add(i);
    }

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
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        i.incrementAndGet();
//        i.incrementAndGet();
    }

    @Override
    public void run() {
        while (true) increment();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicClassTest act = new AtomicClassTest();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(act);
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        s.schedule(() -> {
            System.out.println("Aborting...");
            executor.shutdown();
            s.shutdown();
            System.out.println(Arrays.toString(act.iSet.toArray()));
            System.exit(0);
        }, 5, TimeUnit.SECONDS);
        while (true) {
            int value = act.getValue();
            if (!act.add(value)) {
                System.out.println(value);
                System.exit(0);
            }
            Thread.sleep(50);
            // the value can still be odd
            /*if (value % 2 != 0) {
                System.out.println(value);
                System.exit(0);
            }*/
        }
    }
}
