package com.wangy.review.concurrency.component;

import java.util.concurrent.CyclicBarrier;

/**
 * Java并发编程的艺术 第八章8.2节代码清单8-4
 * <p>
 *     <code>BarrierAction</code>的使用
 * <p>
 * 输出: <p>
 *     3 <p> 1 <p> 2 <p>
 * 或者: <p>
 *     3 <p> 2 <p> 1
 *
 * @author wangy
 * @version 1.0
 * @date 2022/12/5 / 11:40
 */
public class CyclicBarrierWithBarrierAction_2 {
    static CyclicBarrier c = new CyclicBarrier(2, new A());

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
                    //
                }
                System.out.println(1);
            }

        }).start();

        try {
            c.await();

        } catch (Exception e) {
            // ..
        }

        System.out.println(2);

    }

    static class A implements Runnable {
        @Override
        public void run() {
            System.out.println(3);

        }
    }
}
