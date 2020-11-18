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
    /** CyclicBarrier的构造器任务总是会先执行完毕 */
    static CyclicBarrier c1 = new CyclicBarrier(2, () -> {
        sb.append(3);
    });


    private static final int ASSERT_VALUE = 312;

    /** parties 和 barrierAction的执行顺序 */
    private static int runSeq() {
        Thread t = new Thread(() -> {
            try {
                c1.await();
            } catch (Exception e) {
                // ignore;
            }
            sb.append(1);
        });
        t.start();
        try {
            c1.await();
            sb.append(2);
            t.join();
        } catch (Exception e) {
            // ignore
        }
        return Integer.parseInt(sb.toString()) | (sb.delete(0, sb.length())).length();
    }

    static void alwaysReturn() {
        for (; ; ) {
            int r;
            if ((r = runSeq()) != ASSERT_VALUE) {
                // should be 321
                System.out.println(r);
                return;
            }
        }
    }


    public static void main(String[] args) {

        alwaysReturn();
    }
}
