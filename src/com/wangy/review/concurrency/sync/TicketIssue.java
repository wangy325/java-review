package com.wangy.review.concurrency.sync;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 在使用同步时，将域设置为"私有的"是非常有必要的，否则，synchronized关键字不能组织其他任务直接访问域
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 16:55
 */
public class TicketIssue {

    protected final Tick tick = new Tick(0);
    private final List<Future<TV>> resultList = new ArrayList<>();

    static class Tick {
        // 一般将共享资源设置为私有以降低同步问题的复杂性
        int tickCount;
        boolean isTickSupply = true;

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

    @Setter
    @Getter
    static class TV {
        Thread t;
        Integer v = 0;
    }

    static class Purchase implements Callable<TV> {

        // 线程抢到的票计数器
        // 线程内部存储一般声明为static
        private static final ThreadLocal<TV> tl = ThreadLocal.withInitial(TV::new);

        private final Tick tick;

        Purchase(Tick tick) {
            this.tick = tick;
        }

        @Override
        public TV call() {
            while (true) {
                synchronized (tick) {
                    TV tv = tl.get();
                    tv.setT(Thread.currentThread());
                    if (tick.getTick()) {
                        tv.setV((tv.getV() == null ? 0 : tv.getV()) + 1);
                        tl.set(tv);
//                        System.out.println(Thread.currentThread().getName() + " 抢到票, 余票数: " + tick.getTickCount());
                        try {
                            // 给其他线程机会
                            tick.wait(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!tick.isTickSupply) break;
                    }
                }
            }
            return tl.get();
        }
    }

    void multiPurchase(int threadCount) throws InterruptedException, ExecutionException {

        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            Future<TV> future = pool.submit(new Purchase(tick));
            resultList.add(future);
        }
        pool.shutdown();

        int sum = 0;
        for (int i = 0; i < resultList.size(); i++) {
            TV tv = resultList.get(i).get();
            System.out.println(tv.getT().getName() + " 抢到票：" + tv.getV() + "张");
            sum = sum + tv.getV();
        }
        System.out.println("已购票数：" + sum);
    }

    /** 放票 */
    void singleSupply(int count) {

        new Thread(() -> {
            for (int i = 0; i < count; i++) {
                // 此处不使用同步不影响最终结果，线程会一直抢票
                // 即使某刻读取到了未刷新的tickCount数值，最终都会抢到票
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


    public static void main(String[] args) throws Exception {

        TicketIssue ti = new TicketIssue();
        ti.singleSupply(10);
        ti.multiPurchase(2);
    }
}
