package com.wangy.review.concurrency.component;

import lombok.SneakyThrows;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/11 / 01:28
 */
public class TestScheduledPoolExecutor {

    private AtomicInteger sequence = new AtomicInteger(0);

    private void s() {
        System.out.println(Thread.currentThread() + " " + sequence.getAndIncrement());
    }

    private void i() {
        while (true) {
            // do nothing
        }
    }

    void test1() {
        ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(1);

//        service.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        service.schedule(this::s, 2, TimeUnit.HOURS);
        service.schedule(this::s, 1, TimeUnit.SECONDS);

        service.shutdown();
    }

    @SneakyThrows
    void cancelSchedule() {
        ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(1);

        service.setRemoveOnCancelPolicy(true);
        // task to cancelled
        service.schedule(this::s, 10, TimeUnit.SECONDS);
        BlockingQueue<Runnable> queue = service.getQueue();
        Runnable task = queue.peek();
        if (task instanceof RunnableScheduledFuture) {
            ((FutureTask<?>) task).cancel(false);
        }

        service.schedule(this::s, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(2);
        // should be 1
        System.out.println("queue size: " + queue.size());

        service.shutdown();
        // removed by onShutdown hook method
        System.out.println("queue size: " + queue.size());
    }

    @SneakyThrows
    void shutdownPolicy() {
        ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(2);

        // 如果任务在shutdown()之后仍在delay，那么将值设置为false可以取消任务的执行
        // 其默认值为true
        service.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        service.schedule(this::s, 1, TimeUnit.MILLISECONDS);

        // 如果是周期执行的任务，将此值设置为true可以在调用shutdown()之后让其继续执行，否则结束执行
        // 其默认值为false
        service.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
        service.scheduleWithFixedDelay(this::s, 2, 1, TimeUnit.SECONDS);

        service.shutdown();
        TimeUnit.SECONDS.sleep(10);
        // shutdownNow interrupt all tasks
        service.shutdownNow();
        // could be true or false
        System.out.println(service.isTerminated());
    }

    public static void main(String[] args) {
        TestScheduledPoolExecutor ts = new TestScheduledPoolExecutor();

        ts.test1();
//        ts.cancelSchedule();
//        ts.shutdownPolicy();
    }

}
