package com.wangy.review.concurrency.sync;

import java.util.concurrent.TimeUnit;

/**
 * 一个误用<code>volatile</code>可见性的例子
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/16 / 18:03
 * @see AtomicClassTest
 */
public class VolatileSerialNumberGenerator {
    private static final int SIZE = 10;
    private static volatile int serial = 0;
    private static volatile boolean shutdown = false;
    private static CircularSet serials = new CircularSet(1000);

    static class Generator {
        static int nextSerialNumber() {
            // not thread safe
            return serial++;
        }
    }

    static class CircularSet {
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

        public synchronized void add(int i) {
            array[index] = i;
            index = ++index % len;
        }

        public synchronized boolean contains(int i) {
            for (int i1 : array) {
                if (i1 == i) return true;
            }
            return false;
        }
    }

    static class SerialNumberChecker implements Runnable {
        @Override
        public void run() {
            while (!shutdown) {
                int nextSerialNumber = Generator.nextSerialNumber();
                if (serials.contains(nextSerialNumber)) {
                    System.out.println(Thread.currentThread() + " Duplicate: " + nextSerialNumber);
                    shutdown = true;
                    System.exit(0);
                }
                serials.add(nextSerialNumber);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < SIZE; i++) {
            new Thread(new SerialNumberChecker()).start();
        }
        TimeUnit.MILLISECONDS.sleep(70);
       while(true){
       }
    }
}
