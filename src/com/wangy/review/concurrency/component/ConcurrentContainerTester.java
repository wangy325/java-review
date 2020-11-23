package com.wangy.review.concurrency.component;

import com.wangy.helper.util.Generated;
import com.wangy.helper.util.RandomGenerator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Framework to test performance of concurrent container
 * <p>
 * from tij
 * <p>
 * 这个框架使用了「模版方法设计模式」
 *
 * @author mindview
 * @version 1.0
 * @date 2020/11/21 / 21:30
 */
public abstract class ConcurrentContainerTester<C> {

    static int testReps = 10;
    static int testCycles = 1000;
    static int containerSize = 1000;
    static ExecutorService exec = Executors.newCachedThreadPool();

    C testContainer;
    String testId;
    int nReaders;
    int nWriters;
    volatile long readResult = 0;
    volatile long readTime = 0;
    volatile long[] readTimes = new long[testReps];
    volatile long writeTime = 0;
    volatile long[] writeTimes = new long[testReps];
    CountDownLatch endLatch;
    Integer[] writeData;

    ConcurrentContainerTester(String testId, int nReaders, int nWriters) {
        this.testId = testId;
        this.nReaders = nReaders;
        this.nWriters = nWriters;
        writeData = Generated.array(Integer.class, new RandomGenerator.Integer(), containerSize);
        for (int i = 0; i < testReps; i++) {
            runTest();
            readTimes[i] = readTime;
            writeTimes[i] = writeTime;
            // reset read and write time
            readTime = 0;
            writeTime = 0;
        }
        // calculate avg read/write time
        long writeSum = 0, readSum = 0;
        for (int i = 0; i < testReps; i++) {
            writeSum += writeTimes[i];
            readSum += readTimes[i];
        }
        long writeAvg = writeSum / testReps;
        long readAvg = readSum / testReps;
        System.out.printf("%-30s %14d %14d\n",
            testId + " " + nReaders + "r " + nWriters + "w",
            readAvg, writeAvg);
        if (readAvg != 0 && writeAvg != 0)
            System.out.printf("%-30s %14d\n",
                "readTime + writeTime =", readAvg + writeAvg);
    }


    abstract C containerInitializer();

    abstract void startReadersAndWriters();

    void runTest() {
        endLatch = new CountDownLatch(nReaders + nWriters);
        testContainer = containerInitializer();
        startReadersAndWriters();
        try {
            endLatch.await();
        } catch (InterruptedException ex) {
            System.out.println("endLatch interrupted");
        }
    }

    abstract class TestTask implements Runnable {
        abstract void test();

        /** implementation methods must be synchronized! */
        abstract void putResults();

        long duration;

        @Override
        public void run() {
            long startTime = System.nanoTime();
            test();
            duration = System.nanoTime() - startTime;
            putResults();
            endLatch.countDown();
        }
    }

    public static void initMain(String[] args) {
        if (args != null) {
            if (args.length > 0)
                testReps = new Integer(args[0]);
            if (args.length > 1)
                testCycles = new Integer(args[1]);
            if (args.length > 2)
                containerSize = new Integer(args[2]);
        }
        System.out.printf("%-30s %14s %14s\n",
            "Type", "Read time", "Write time");
    }

}
