package com.wangy.review.concurrency.component;

import java.util.concurrent.CyclicBarrier;

/**
 * "屏障"开启后，所有线程均可运行，但是无法保证线程的执行顺序
 * <p>
 * "屏障"可以重复使用
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/16 / 18:32
 */
public class TestCyclicBarrier {

    private static StringBuffer sb = new StringBuffer();
    static CyclicBarrier c = new CyclicBarrier(2, () -> sb.append(3));
    private static final int ASSERT_VALUE = 312;

    static int run() {
        Thread t = new Thread(() -> {
            try {
                c.await();
            } catch (Exception e) {
                // ignore;
            }
            sb.append(1);
        });
        t.start();
        try {
            c.await();
            sb.append(2);
            t.join();
        } catch (Exception e) {
            // ignore
        }
        return Integer.parseInt(sb.toString()) | (sb.delete(0, sb.length())).length();
    }

    public static void main(String[] args) {
        for (; ; ) {
            int r;
            if ((r = run()) != ASSERT_VALUE) {
                // should be 321
                System.out.println(r);
                return;
            }
        }
    }
}
