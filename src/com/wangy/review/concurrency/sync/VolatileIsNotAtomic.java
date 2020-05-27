package com.wangy.review.concurrency.sync;

/**
 * volatile 不能保证原子性<br>
 * 在本例中你将看到使用无锁机制保证线程有序运行
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/16 / 15:45
 */
public class VolatileIsNotAtomic {

    private int sum;

    void increase() {
        sum++;
        System.out.println(Thread.currentThread() + ": " + sum);
    }


    void singleThread() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increase();
            }
        });
        thread.start();
        thread.join();
        System.out.println(sum);
    }

    void multiThread1() throws InterruptedException {
        Thread mt = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Thread t = new Thread(() -> {
                    for (int j = 0; j < 10; j++) {
                        increase();
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mt.start();
        mt.join();
        System.out.println(sum);
    }

    void multiThread2() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    increase();
                }
            });
            thread.start();
            // 使用join()保证有序性，此时可以不需要同步
            thread.join();
        }
        // 主线程等待所有的子线程结束
        System.out.println(sum);
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileIsNotAtomic va = new VolatileIsNotAtomic();
//        va.singleThread();
//        va.multiThread1();
        va.multiThread2();


    }
}
