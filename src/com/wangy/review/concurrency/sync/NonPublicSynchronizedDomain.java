package com.wangy.review.concurrency.sync;

/**
 * 如果使用非私有域的共享资源，那么线程可以不通过方法而直接访问资源，增加竞争风险
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 16:55
 */
public class NonPublicSynchronizedDomain {

    static class Tick {
        // non-public domain can be accessed and modified directly in another thread
        // without calling method, which may increase the cost of synchronize
        int tickCount;

        public Tick(int tick) {
            this.tickCount = tick;
        }

        synchronized boolean getTick() {
            if (tickCount > 0) {
                Thread.yield();
                tickCount--;
                System.out.println(Thread.currentThread() + "get a tick, left: " + tickCount);
                return true;
            }
            return false;
        }
    }

    static class Purchase implements Runnable {
        private Tick tick;

        public Purchase(Tick tick) {
            this.tick = tick;
        }

        @Override
        public void run() {
            while (true) {
                tick.getTick();
                Thread.yield();
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Tick tick = new Tick(2);
        // modify shared resource in main thread
        Thread t1 = new Thread(new Supply(tick));
        t1.start();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Purchase(tick));
            t.start();
        }
    }
}
