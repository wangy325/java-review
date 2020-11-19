package com.wangy.review.concurrency.component;

import com.wangy.helper.util.BasicGenerator;
import com.wangy.helper.util.Fat;
import com.wangy.helper.util.Generator;

import java.util.Queue;
import java.util.concurrent.*;
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
            try {
                while (db != null) {
                    if (db.isFull()) {
                        db = ex.exchange(db);
                    } else {
                        db.addToBuffer(gen.next());
                    }
                }
            } catch (InterruptedException e) {
                // right to exit here
            }
        }
    }

    private class EmptyTask<T> implements Runnable {

        private DataBuffer<T> db;
        private final Exchanger<DataBuffer<T>> ex;

        public EmptyTask(DataBuffer<T> db, Exchanger<DataBuffer<T>> ex) {
            this.db = db;
            this.ex = ex;
        }

        @Override
        public void run() {
            try {
                while (ec.intValue() < SIZE) {
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

    private AtomicInteger ec = new AtomicInteger();
    private final int SIZE = 10;

    void test() throws InterruptedException {
        Exchanger<DataBuffer<Fat>> xh = new Exchanger<>();
        Generator<Fat> generator = BasicGenerator.create(Fat.class);

        DataBuffer<Fat> fullBuffer = new DataBuffer<>(new ArrayBlockingQueue<>(SIZE), generator,SIZE,false);
        DataBuffer<Fat> emptyBuffer = new DataBuffer<>(new ArrayBlockingQueue<>(SIZE), null, SIZE,true);
        ExecutorService pool = Executors.newCachedThreadPool();

        Future<?> t1 = pool.submit(this.new FillTask<>(fullBuffer, generator, xh));
        Future<?> done = pool.submit(this.new EmptyTask<>(emptyBuffer, xh));
        for (; ; ) {
            if (done.isDone()) {
                t1.cancel(true);
                break;
            }
        }
        System.out.println(ec.intValue());
        System.out.println("++++++++++++++++++");
        Queue<Fat> buffer = fullBuffer.getBuffer();
        for (Fat fat : buffer) {
            System.out.println(fat);
        }

        System.out.println("++++++++++++++++++");
        Queue<Fat> buffer1 = emptyBuffer.getBuffer();
        for (Fat fat : buffer1) {
            System.out.println(fat);
        }
        pool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        BufferSwap bs = new BufferSwap();
        bs.test();

    }
}
