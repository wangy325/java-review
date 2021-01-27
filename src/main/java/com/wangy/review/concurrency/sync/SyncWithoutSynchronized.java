package com.wangy.review.concurrency.sync;

/**
 * volatile 不能保证原子性<br>
 * 在本例中你将看到使用无锁机制保证线程有序访问<br>
 * <p>
 * 关键字volatile
 * <ul>
 * <li>保证可见性</li>
 * <li>取消指令重排</li>
 * <li>不保证原子性</li>
 * </ul>
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/16 / 15:45
 */
public class SyncWithoutSynchronized {

    private int sum;

    void increase() {
        sum++;
        System.out.println(Thread.currentThread() + ": " + sum);
    }


    /** 单线程模式 */
    void singleThread() throws InterruptedException {
        Thread task = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                increase();
            }
        });
        task.start();
        // 等待task执行完成
        task.join();
        System.out.println(sum);
    }

    /** 线程mt派生出10个线程 */
    void multiThread1() throws InterruptedException {
        Thread mt = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Thread t = new Thread(() -> {
                    for (int j = 0; j < 1; j++) {
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

    /** 上一种方式的简化版 */
    void multiThread2() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1; j++) {
                    increase();
                }
            });
            thread.start();
            // 使用join()保证有序性，此时可以不需要同步
            // join() 保证了happens-before原则
            thread.join();
        }
        // 主线程等待所有的子线程结束
        System.out.println(sum);
    }

    public static void main(String[] args) throws InterruptedException {
        SyncWithoutSynchronized va = new SyncWithoutSynchronized();
//        va.singleThread();
//        va.multiThread1();
        va.multiThread2();


    }
}
