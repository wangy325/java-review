package com.wangy.review.container.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * undone demo
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/9 / 11:23
 */
public class SynchronizedViewTest {


    /** 同步集合 */
    private static List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>() {{
        add(0);
    }});
    /** 非同步集合 */
    private static List<Integer> list = new ArrayList<Integer>() {{
        add(0);
    }};
    /** 同步集合 */
    private static Vector<Integer> vector = new Vector<Integer>() {{
        add(0);
    }};

    private AtomicInteger cal_count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        SynchronizedViewTest svt = new SynchronizedViewTest();

        svt.testSynchronizedList();
        /*while (true){
//            if (svt.testExecutorsShutdown() != 2450) break;
            System.out.println(svt.testExecutorsShutdown());
            Thread.sleep(2000);
        }*/

//        unsafeIncrAndReplace(vector);
//        System.out.println(vector.get(0));
//        unsafeIncrAndReplace(list);
//        System.out.println(list.get(0));
    }

    void testSynchronizedList() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            unsafeIncrAndReplace(synchronizedList);

           /* if (synchronizedList.get(0) != 2450){
                System.out.println(synchronizedList.get(0) + ": " + count);
                break;
            }*/
            TimeUnit.SECONDS.sleep(1);
            System.out.println(synchronizedList.get(0) + ": " + cal_count);
        }
    }

    int testExecutorsShutdown() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        AtomicInteger i = new AtomicInteger(0);
        executorService.execute(() -> {
            for (int j = 0; j < 50; j++) {
                i.set(i.intValue() + j);
            }
        });
        executorService.execute(() -> {
            for (int j = 0; j < 50; j++) {
                i.set(i.intValue() + j);
            }
        });
        executorService.shutdown();
//        executorService.awaitTermination(2, TimeUnit.SECONDS);
        return i.intValue();
    }

    /**
     * 线程不安全的方法，如果使用线程安全的集合，可以获得预期的结果
     *
     * @param list
     */
    void unsafeIncrAndReplace(List<Integer> list) throws InterruptedException {
        list.set(0, 0);
        cal_count.set(0);
        ExecutorService executorService = Executors.newFixedThreadPool(1);


        // thread-1
        executorService.execute(() -> {
            for (int i = 0; i < 50; i++) {
                cal_count.getAndIncrement();
                list.set(0, list.get(0) + i);
            }
        });

        // thread-2
        executorService.execute(() -> {
            for (int i = 0; i < 50; i++) {
                cal_count.getAndIncrement();
                list.set(0, list.get(0) + i);
            }
        });

        executorService.shutdown();
//        executorService.awaitTermination(2, TimeUnit.SECONDS);
    }
}
