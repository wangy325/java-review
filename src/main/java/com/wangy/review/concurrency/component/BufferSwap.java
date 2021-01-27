package com.wangy.review.concurrency.component;

import com.wangy.helper.util.BasicGenerator;
import com.wangy.helper.util.Fat;
import com.wangy.helper.util.Generator;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 演化自{@link java.util.concurrent.Exchanger}类文档示例，在两个任务之间交换缓存
 * 保证任务一直执行而不阻塞/退出
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/19 / 12:53
 */
public class BufferSwap {

    private class FillTask<T> implements Runnable {
        private DataBuffer<T> db;
        private final Exchanger<DataBuffer<T>> ex;
        private final Generator<T> gen;

        public FillTask(DataBuffer<T> db, Generator<T> gen, Exchanger<DataBuffer<T>> ex) {
            this.db = db;
            this.gen = gen;
            this.ex = ex;
        }

        @Override
        public void run() {
            while (db != null) {
                if (db.isFull()) {
                    try {
                        // may cancel in here with InterruptedException thrown
                        db = ex.exchange(db);
                    } catch (InterruptedException e) {
                        // right to exit here
                        break;
                    }
                } else {
                    // or cancel in here with interrupt status set
                    if (Thread.currentThread().isInterrupted())
                        break;
                    db.addToBuffer(gen.next());
                }
            }
        }
    }

    private class EmptyTask<T> implements Runnable {

        private DataBuffer<T> db;
        private final Exchanger<DataBuffer<T>> ex;
        private final int ecLimit;

        public EmptyTask(DataBuffer<T> db, Exchanger<DataBuffer<T>> ex, int limit) {
            this.db = db;
            this.ex = ex;
            this.ecLimit = limit;
        }

        @Override
        public void run() {
            try {
                while (ec.intValue() < ecLimit) {
                    if (db.isEmpty()) {
                        db = ex.exchange(db);
                        ec.incrementAndGet();
                    } else {
                        db.takeFromBuffer();
                    }
                }
            } catch (InterruptedException e) {
                // exit by interrupted
            }
        }
    }

    /** 交换缓存的次数，用来限制程序的运行 */
    private final AtomicInteger ec = new AtomicInteger();

    /**
     * @param size  the buffer size
     * @param limit the exchange time limit
     */
    void test(int size, int limit) {
        Exchanger<DataBuffer<Fat>> xh = new Exchanger<>();
        Generator<Fat> generator = BasicGenerator.create(Fat.class);
        // ignore class check
        // can not solve the issue actually...
        DataBuffer<Fat> fullBuffer, emptyBuffer;
        try {
            fullBuffer = new DataBuffer(LinkedList.class, size, generator);
            emptyBuffer = new DataBuffer(LinkedList.class, size);
        } catch (Exception e) {
            System.out.println("initialization failure");
            return;
        }
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<?> t1 = pool.submit(this.new FillTask(fullBuffer, generator, xh));
        Future<?> done = pool.submit(this.new EmptyTask<>(emptyBuffer, xh, limit));
        for (; ; ) {
            if (done.isDone()) {
                t1.cancel(true);
                break;
            }
        }
        Queue<Fat> full = fullBuffer.getBuffer();
        System.out.print("fillTask's buffer: ");
        for (Fat fat : full) {
            System.out.printf("%s\t", fat);
        }
        System.out.println();
        Queue<Fat> empty = emptyBuffer.getBuffer();
        System.out.print("emptyTask's buffer: ");
        for (Fat fat : empty) {
            System.out.printf("%s\t", fat);
        }
        // shutdown可以放在cancel之后的任意位置
        pool.shutdown();
    }

    public static void main(String[] args) {
        BufferSwap bs = new BufferSwap();
        bs.test(10, 100);
    }
}
