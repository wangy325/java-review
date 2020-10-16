package com.wangy.review.concurrency.sync;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发编程需要同时保证原子性，可见性和有序性，这样才是并发安全的代码<br>
 * <p>
 * volatile关键字的使用场景有限，就算是在经典的关闭线程场景下，应对复杂的逻辑，其作用也非常有限
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/16 / 18:03
 * @see AtomicIntegerTest
 */
public class VolatileEffectLittle {
    private int serial = 0;
    /** 在此逻辑下，将字段设置为volatile也收效甚微 */
    private volatile boolean shutdown = false;
    private AtomicInteger shutdownKeeper = new AtomicInteger(0);
    private final CircularSet serials = new CircularSet(1000);

    /** 同步此方法以解决并发问题 */
    int nextSerialNumber() {
        // not thread safe
        return serial++;
    }

    /**
     * 此容器用于存放{@link VolatileEffectLittle#nextSerialNumber}类生成的数
     * 每生成一个序列数，将其存放至容器中
     */
    class CircularSet {
        private int[] array;
        private int len;
        private int index = 0;

        public CircularSet(int size) {
            array = new int[size];
            this.len = size;
            for (int i = 0; i < size; i++) {
                array[i] = -1;
            }
        }

        synchronized void add(int i) {
            array[index] = i;
            index = ++index % len;
        }

        synchronized boolean contains(int i) {
            for (int i1 : array) {
                if (i1 == i) return true;
            }
            return false;
        }
    }

    class SerialNumberChecker implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            /*
             * 此逻辑下，将shutdown设置为volatile以期望线程结束时shutdownKeeper的值总是1
             * 实际上期望并不能满足
             * 诚然，在线程1出现第一次讹误时，shutdown被置为true，但是volatile只能保证在其之后的线程2
             * 对shutdown的读是可见的
             * 但并不能保证线程3总是在线程1修改完成shutdown之后再去读取，因此
             * shutdownKeeper的值还是可能>1
             * 修改方法为同步while语句块
             * 一旦同步while语句快之后，volatile关键字的作用就被削弱了（被同步取代）
             * 要注意同步监视器的选择，可以选择serials的监视器而不能选择this
             * this是SerialNumberChecker对象，而每个线程的都对应一个this！
             */
            while (!shutdown) {
                //由于自增方法没有同步，多线程读写的时候出现讹误
                int nextSerialNumber = nextSerialNumber(); // unlock
                if (serials.contains(nextSerialNumber)) {
                    System.out.println(Thread.currentThread() + " Duplicate Serial Number: " + nextSerialNumber);
                    shutdown = true;
                    shutdownKeeper.incrementAndGet();
                    break;
                }
                serials.add(nextSerialNumber);
            }
        }
    }

    public static void main(String[] args) {
        // 因为shutdown安全性的问题，此循环终会结束
        while (true) {
            int i = test();
            if (i > 1) {
                System.out.println(i);
                break;
            }
        }
    }

    public static int test() {
        VolatileEffectLittle vfl = new VolatileEffectLittle();
        ExecutorService pool = Executors.newCachedThreadPool();
        int SIZE = 10;
        for (int i = 0; i < SIZE; i++) {
            pool.execute(vfl.new SerialNumberChecker());
        }
        pool.shutdown();

        // 等待pool任务执行完
        while (Thread.activeCount() > 2) Thread.yield();
        return vfl.shutdownKeeper.get();
    }
}
