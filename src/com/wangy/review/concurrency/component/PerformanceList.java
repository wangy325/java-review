package com.wangy.review.concurrency.component;

import com.wangy.helper.util.CountingIntegerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Rough comparison of thread-safe List performance.
 *
 * @author mindview
 * @version 1.0
 * @date 2020/11/22 / 14:31
 */
public class PerformanceList {
    abstract class ListTest extends ConcurrentContainerTester<List<Integer>> {
        ListTest(String testId, int nReaders, int nWriters) {
            super(testId, nReaders, nWriters);
        }

        class Reader extends TestTask {
            long result = 0;

            @Override
            void test() {
                for (long i = 0; i < testCycles; i++) {
                    for (int index = 0; index < containerSize; index++)
                        result += testContainer.get(index);
                }
            }

            @Override
            synchronized void putResults() {
                readResult += result;
                readTime += duration;
            }
        }

        class Writer extends TestTask {
            @Override
            void test() {
                for (long i = 0; i < testCycles; i++) {
                    for (int index = 0; index < containerSize; index++)
                        testContainer.set(index, writeData[index]);
                }
            }

            @Override
            synchronized void putResults() {
                writeTime += duration;
            }
        }

        @Override
        void startReadersAndWriters() {
            // multi-thread read & write
            for (int i = 0; i < nReaders; i++)
                exec.execute(new Reader());
            for (int i = 0; i < nWriters; i++)
                exec.execute(new Writer());
        }
    }

    class SynchronizedArrayListTest extends ListTest {
        @Override
        List<Integer> containerInitializer() {
            return Collections.synchronizedList(
                new ArrayList<>(new CountingIntegerList(containerSize)));
        }

        SynchronizedArrayListTest(int nReaders, int nWriters) {
            super("Synched ArrayList", nReaders, nWriters);
        }
    }

    class CopyOnWriteArrayListTest extends ListTest {
        @Override
        List<Integer> containerInitializer() {
            return new CopyOnWriteArrayList<>(new CountingIntegerList(containerSize));
        }

        CopyOnWriteArrayListTest(int nReaders, int nWriters) {
            super("CopyOnWriteArrayList", nReaders, nWriters);
        }
    }

    void test(String[] args) {
        ConcurrentContainerTester.initMain(args);
        new SynchronizedArrayListTest(10, 0);
        new SynchronizedArrayListTest(9, 1);
        new SynchronizedArrayListTest(5, 5);
        new CopyOnWriteArrayListTest(10, 0);
        new CopyOnWriteArrayListTest(9, 1);
        new CopyOnWriteArrayListTest(5, 5);
        ConcurrentContainerTester.exec.shutdown();
    }

    public static void main(String[] args) {
        PerformanceList lp = new PerformanceList();

        lp.test(new String[]{"10", "1000", "1000"});
    }
}
