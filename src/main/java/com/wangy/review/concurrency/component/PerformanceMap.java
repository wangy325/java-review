package com.wangy.review.concurrency.component;

import com.wangy.helper.util.CountingGenerator;
import com.wangy.helper.util.MapData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Rough comparison of thread-safe Map performance.
 * <p>
 * {@link ConcurrentHashMap}性能完爆{@link HashMap}，也好于{@link ConcurrentSkipListMap}
 *
 * @author mindview
 * @version 1.0
 * @date 2020/11/22 / 22:58
 */
public class PerformanceMap {
    abstract class MapTest
        extends ConcurrentContainerTester<Map<Integer, Integer>> {
        MapTest(String testId, int nReaders, int nWriters) {
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
                        testContainer.put(index, writeData[index]);
                }
            }

            @Override
            synchronized void putResults() {
                writeTime += duration;
            }
        }

        @Override
        void startReadersAndWriters() {
            for (int i = 0; i < nReaders; i++)
                exec.execute(new Reader());
            for (int i = 0; i < nWriters; i++)
                exec.execute(new Writer());
        }
    }

    class SynchronizedHashMapTest extends MapTest {
        @Override
        Map<Integer, Integer> containerInitializer() {
            return Collections.synchronizedMap(
                new HashMap<>(
                    MapData.map(
                        new CountingGenerator.Integer(),
                        new CountingGenerator.Integer(),
                        containerSize)));
        }

        SynchronizedHashMapTest(int nReaders, int nWriters) {
            super("Synched HashMap", nReaders, nWriters);
        }
    }

    class ConcurrentHashMapTest extends MapTest {
        @Override
        Map<Integer, Integer> containerInitializer() {
            return new ConcurrentHashMap<>(
                MapData.map(
                    new CountingGenerator.Integer(),
                    new CountingGenerator.Integer(),
                    containerSize));
        }

        ConcurrentHashMapTest(int nReaders, int nWriters) {
            super("ConcurrentHashMap", nReaders, nWriters);
        }
    }

    class ConcurrentSkipListMapTest extends MapTest {

        ConcurrentSkipListMapTest(int nReaders, int nWriters) {
            super("ConcurrentSkipListMap", nReaders, nWriters);
        }

        @Override
        Map<Integer, Integer> containerInitializer() {
            // elements in ConcurrentSkipListMap must be comparable
            return new ConcurrentSkipListMap<>(
                MapData.map(
                    new CountingGenerator.Integer(),
                    new CountingGenerator.Integer(),
                    containerSize));
        }
    }

    void test(String[] args) {
        ConcurrentContainerTester.initMain(args);
        new SynchronizedHashMapTest(10, 0);
        new SynchronizedHashMapTest(9, 1);
        new SynchronizedHashMapTest(5, 5);
        new ConcurrentHashMapTest(10, 0);
        new ConcurrentHashMapTest(9, 1);
        new ConcurrentHashMapTest(5, 5);
        new ConcurrentSkipListMapTest(10, 0);
        new ConcurrentSkipListMapTest(9, 1);
        new ConcurrentSkipListMapTest(5, 5);
        ConcurrentContainerTester.exec.shutdown();
    }

    public static void main(String[] args) {
        PerformanceMap pm = new PerformanceMap();
        pm.test(new String[]{"10", "1000", "1000"});
    }
}
