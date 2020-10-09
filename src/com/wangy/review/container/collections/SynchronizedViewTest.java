package com.wangy.review.container.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * undone demo
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/9 / 11:23
 */
public class SynchronizedViewTest {


    private static List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>() {{
        add(0);
    }});
    private static List<Integer> list = new ArrayList<Integer>() {{
        add(0);
    }};

    public static void main(String[] args) {
        unsafeIncrAndReplace(synchronizedList);
        System.out.println(synchronizedList.get(0));
        unsafeIncrAndReplace(list);
        System.out.println(list.get(0));
    }

    /**
     * 线程不安全的方法，如果使用线程安全的集合，可以获得预期的结果
     *
     * @param list
     */
    static void unsafeIncrAndReplace(List<Integer> list) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //
        executorService.execute(() -> {
            for (int i = 0; i < 50; i++) {
                int y = list.get(0);
                list.clear();
                list.add(y + i);

            }
        });

        executorService.execute(() -> {
            for (int i = 0; i < 50; i++) {
                int y = list.get(0);
                list.clear();
                list.add(y + i);
            }
        });

        executorService.shutdown();
    }
}
