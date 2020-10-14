package com.wangy.review.concurrency.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


/**
 * @author wangy
 * @version 1.0
 * @date 2020/10/14 / 16:59
 */
public class TicketIssueWithFutureTask extends TicketIssue {

    private final HashMap<Thread, Future<Integer>> resultMap = new HashMap<>();

    static class Purchase implements Callable<Integer> {

        // 线程抢到的票计数器
        // 线程内部存储一般声明为static
        private static ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 0);

        private final Tick tick;

        Purchase(Tick tick) {
            this.tick = tick;
        }

        @Override
        public Integer call() {
            while (true) {
                synchronized (tick) {
                    if (tick.getTick()) {
                        tl.set(tl.get() + 1);
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

    @Override
    void multiPurchase(int threadCount) throws ExecutionException, InterruptedException {
        for (int i = 0; i < threadCount; i++) {
            // FutureTask实现了Runnable，可以在显式线程执行之后再通过其获取返回值
            // 当然，也可以通过执行器执行
            FutureTask<Integer> ft = new FutureTask<>(new Purchase(tick));
            Thread t = new Thread(ft);
            t.start();
            resultMap.put(t, ft);
        }
        int sum = 0;
        for (Map.Entry<Thread, Future<Integer>> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey().getName() + " 抢到票：" + entry.getValue().get() + "张");
            sum = sum + entry.getValue().get();
        }
        System.out.println("已购票数：" + sum);
    }


    public static void main(String[] args) throws Exception {
        TicketIssueWithFutureTask ti = new TicketIssueWithFutureTask();

        ti.singleSupply(10);
        ti.multiPurchase(12);
    }
}
