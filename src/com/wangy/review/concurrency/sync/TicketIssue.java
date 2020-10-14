package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在使用同步时，将域设置为"私有的"是非常有必要的，否则，synchronized关键字不能组织其他任务直接访问域
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 16:55
 */
public class TicketIssue {

    AtomicInteger tickGet = new AtomicInteger(0);
    Tick tick = new Tick(0);

    static class Tick {
        // 将共享资源设置为私有以降低同步问题的复杂性
        private int tickCount;
        private boolean isTickSupply = true;

        public Tick(int tick) {
            this.tickCount = tick;
        }

        boolean getTick() {
            if (isTick()) {
                tickCount--;
                if (getTickCount() < 0) {
                    System.out.println("余票数 " + tickCount + "不合法，系统错误！");
                    System.exit(0);
                }
                return true;
            }
            return false;
        }

        // 检查余票
        boolean isTick() {
            return tickCount > 0;
        }

        // 获取余票
        int getTickCount() {
            return tickCount;
        }

        // 停止放票
        void cancelSupply() {
            isTickSupply = false;
        }
    }

    class Purchase implements Runnable {
        private final Tick tick;

        Purchase(Tick tick) {
            this.tick = tick;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (tick) {
                    if (tick.getTick()) {
                        tickGet.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + " 抢到票, 余票数: " + tick.getTickCount());
                        try {
                            tick.wait(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!tick.isTickSupply) break;
                    }
                }
            }
        }
    }

    void multiPurchase(int threadCount, int executeTime) throws InterruptedException {

        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            pool.execute(new Purchase(tick));
        }
        pool.shutdown();
    }

    /** 放票 */
    void singleSupply(int count) {

        new Thread(() -> {
            for (int i = 0; i < count; i++) {
                // TODO：此处直接非同步修改域是否安全？
                tick.tickCount++;
                // 降低出票速率
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 停止放票
            tick.cancelSupply();
        }).start();
    }


    public static void main(String[] args) throws InterruptedException {

        TicketIssue npsd = new TicketIssue();
        npsd.singleSupply(10);
        npsd.multiPurchase(11, 10);
//        System.exit(0);
    }
}
