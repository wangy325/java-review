package com.wangy.review.concurrency.component;

import com.wangy.common.helper.CountingIntegerList;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/23 / 14:54
 */
public class PerformanceQueue {
    abstract class QueueTest extends ConcurrentContainerTester<Queue<Integer>> {

        QueueTest(String testId, int nReaders, int nWriters) {
            super(testId, nReaders, nWriters);
        }

        class Writer extends TestTask {

            @Override
            void test() {
                for (long i = 0; i < testCycles; i++) {
                    for (int j = 0; j < containerSize; j++) {
                        testContainer.add(writeData[j]);
                    }
                }
            }

            @Override
            synchronized void putResults() {
                writeTime += duration;
            }
        }

        class Reader extends TestTask {
            long result;

            @Override
            void test() {
                for (long i = 0; i < testCycles; i++) {
                    for (int j = 0; j < containerSize; j++) {
                        Integer poll = testContainer.poll();
                        result += poll == null ? 0 : poll;
                    }
                }
            }

            @Override
            synchronized void putResults() {
                readResult += result;
                readTime += duration;
            }
        }

        @Override
        void startReadersAndWriters() {
            for (int i = 0; i < nReaders; i++) {
                exec.execute(new Reader());
            }
            for (int i = 0; i < nWriters; i++) {
                exec.execute(new Writer());
            }
        }
    }

    class ConcurrentLinkedQueueTest extends QueueTest {

        ConcurrentLinkedQueueTest(int nReaders, int nWriters) {
            super("ConcurrentLinkedQueue", nReaders, nWriters);
        }

        @Override
        Queue<Integer> containerInitializer() {
            return new ConcurrentLinkedQueue<>(
                new CountingIntegerList(containerSize));
        }
    }

    class ConcurrentLinkedDequeTest extends QueueTest {

        ConcurrentLinkedDequeTest(int nReaders, int nWriters) {
            super("ConcurrentLinkedDeque", nReaders, nWriters);
        }

        @Override
        Queue<Integer> containerInitializer() {
            return new ConcurrentLinkedDeque<>(
                new CountingIntegerList(containerSize));
        }
    }

    void test(String[] args) {
        ConcurrentContainerTester.initMain(args);
        new ConcurrentLinkedQueueTest(10, 0);
        new ConcurrentLinkedQueueTest(9, 1);
        new ConcurrentLinkedQueueTest(5, 5);
        new ConcurrentLinkedDequeTest(10, 0);
        new ConcurrentLinkedDequeTest(9, 1);
        new ConcurrentLinkedDequeTest(5, 5);
        ConcurrentContainerTester.exec.shutdown();
    }

    public static void main(String[] args) {
        PerformanceQueue pq = new PerformanceQueue();
        pq.test(new String[]{"1", "1000", "1000"});
    }
}
