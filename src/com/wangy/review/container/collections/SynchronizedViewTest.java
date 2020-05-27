package com.wangy.review.container.collections;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
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


    public static void main(String[] args) {
        SynchronizedList();
    }

    static void SynchronizedList() {

        List<String> list = Collections.synchronizedList(UnmodifiableViewTest.l);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 没有出效果
        for (int i = 0; i < 5; i++) {
            int y = i;
            executorService.execute(()->UnmodifiableViewTest.l.add(0,String.valueOf(y)));
            executorService.execute(()-> System.out.println(UnmodifiableViewTest.l.get(0)));
        }


        /*for (int i = 0; i <5 ; i++) {
            int y = i;
            executorService.execute(()->list.add(0,String.valueOf(y)));
        }

        for (int i = 0; i < 5; i++) {
            executorService.execute(()-> System.out.println(list.get(0)));
        }*/

        executorService.shutdown();
    }
}
