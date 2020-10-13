package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 如果使用非私有域的共享资源，那么线程可以不通过方法而直接访问资源，增加竞争风险
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 16:55
 */
public class NonPublicSynchronizedDomain {

    static AtomicInteger tick_get = new AtomicInteger(0);

    static class Tick {
        // non-public domain can be accessed and modified directly in another thread
        // without calling method, which may increase the cost of synchronize
        int tickCount;

        public Tick(int tick) {
            this.tickCount = tick;
        }

        void getTick() {
            if (tickCount > 0) {
                tickCount--;
                if (tickCount < 0) {
                    System.out.println("余票数 " + tickCount + "不合法，系统错误！");
                    System.exit(0);
                }
            }
        }

        // 检查余票
        boolean isTick() {
            return tickCount > 0;
        }
    }

    static class Purchase implements Runnable {
        private Tick tick;

        Purchase(Tick tick) {
            this.tick = tick;
        }

        @Override
        public void run() {
            while (true) {
                if (!tick.isTick()) continue;
                tick.getTick();
                tick_get.incrementAndGet();
                System.out.println(Thread.currentThread() + "抢到票, 余票数: " + tick.tickCount);
                // 给其他线程抢票机会
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Supply implements Runnable {
        private Tick tick;

        public Supply(Tick tick) {
            this.tick = tick;
        }


        @Override
        public void run() {
            while (true) {
                tick.tickCount++;
                // 降低出票速率
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("程序10s之后终止");
        Tick tick = new Tick(20);
        // modify shared resource in main thread
//        Thread t1 = new Thread(new Supply(tick));
//        t1.start();
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            pool.execute(new Purchase(tick));
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
//        pool.shutdownNow();
        System.out.println("共抢到票: " + tick_get.get());
        System.exit(0);
    }
}
