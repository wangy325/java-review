package com.wangy.review.concurrency.component;

import java.util.concurrent.TimeUnit;

/**
 * 中断任务执行时，使用try-finally块紧跟资源以确保其合适释放是惯用法
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/23 / 16:23
 */
public class InterruptingIdiom {

    /** 需要清理的资源类 */
    static class NeedsCleanup {
        private final int id;

        public NeedsCleanup(int ident) {
            id = ident;
            System.out.println("NeedsCleanup " + id);
        }

        public void cleanup() {
            System.out.println("Cleaning up " + id);
        }
    }

    static class Blocked3 implements Runnable {
        private volatile double d = 0.0;

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    NeedsCleanup n1 = new NeedsCleanup(1);
                    // 在n1之后紧跟try-finally块，保证资源被合理的清除
                    try {
                        System.out.println("Sleeping");
                        TimeUnit.SECONDS.sleep(1);
                        NeedsCleanup n2 = new NeedsCleanup(2);
                        // 同理
                        try {
                            System.out.println("Calculating");
                            // 耗时操作
                            for (int i = 1; i < 2500000; i++) {
                                d = d + (Math.PI + Math.E) / d;
                            }
//                            TimeUnit.SECONDS.sleep(1);
                            System.out.println("Finished time-consuming operation");
                        } finally {
                            n2.cleanup();
                        }
                    } finally {
                        n1.cleanup();
                    }
                }
                System.out.println("Exiting via while() test");
            } catch (InterruptedException e) {
                System.out.println("Exiting via InterruptedException");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("usage: java InterruptingIdiom delay-in-mS");
            System.exit(1);
        }
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(new Integer(args[0]));
        t.interrupt();
    }
}
/* Output: (Sample)
NeedsCleanup 1
Sleeping
NeedsCleanup 2
Calculating
Finished time-consuming operation
Cleaning up 2
Cleaning up 1
NeedsCleanup 1
Sleeping
Cleaning up 1
Exiting via InterruptedException
*///:~
