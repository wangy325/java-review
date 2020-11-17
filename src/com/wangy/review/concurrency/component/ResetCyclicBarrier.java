package com.wangy.review.concurrency.component;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link CyclicBarrier#reset()}方法并不能简单的理解为discard changes and restart all tasks.
 * <p>
 * 其作用是重置屏障，这一操作对<b>正在屏障处等待</b>的线程有不可逆的影响，它们直接抛出{@link BrokenBarrierException}
 * <p>
 * 一旦<code>reset()</code>，就意味着任务没有在barrier处调用{@link CyclicBarrier#await()}，
 * 那么就意味着<code>CyclicBarrier.barrierCommand</code>无法执行了，任务无法全部到达屏障，同时还有可能造成阻塞
 * <p>
 * 本例是一个错误的示范，永远不要在任务内部调用{@link CyclicBarrier#reset()}方法！
 * <p>
 * {@link CyclicBarrier#reset()}方法的文档也建议新建一个屏障比调用reset()方法而言是更好的选择
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/17 / 17:04
 */
public class ResetCyclicBarrier {
    static void reSetBarrierIf(int parties) {
        TaskMayFail[] tasks = new TaskMayFail[parties];
        ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        exec.setKeepAliveTime(0, TimeUnit.SECONDS);
        AtomicInteger ai = new AtomicInteger();
        CyclicBarrier c2 = new CyclicBarrier(parties, () -> {
            // if reset barrier while task is running, the
            // barrier action can not reach in this cycle
            // until relaunch all parties to reach at barrier
            // in next round
            int i = 0;
            int r = tasks[i].runtime;
            while (i < parties) {
                if (r != tasks[i].runtime) {
                    System.out.println(tasks[i] + ":" + tasks[i].runtime + ": " + r);
                    exec.shutdownNow();
                    return;
                }
                r = tasks[i].runtime;
                i++;
            }
        });

        for (int i = 0; i < parties; i++) {
            TaskMayFail taskMayFail = new TaskMayFail(c2, ai);
            tasks[i] = taskMayFail;
            exec.execute(taskMayFail);
        }
    }

    private static class TaskMayFail implements Runnable {
        static Random rand = new Random();
        static int count = 1;
        final CyclicBarrier cb;
        final AtomicInteger reSetCount;
        final int id = count++;
        int runtime = 0;


        public TaskMayFail(CyclicBarrier cb, AtomicInteger reSetCount) {
            this.cb = cb;
            this.reSetCount = reSetCount;
        }

        @Override
        public String toString() {
            return "TaskMayFail-" + id;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (rand.nextBoolean()) {
                        if (rand.nextInt(3) < 1) {
                            throw new ArithmeticException();
                        }
                    }
                    runtime++;
                    cb.await();
                }
            } catch (ArithmeticException ae) {
                reSetCount.incrementAndGet();
                while (cb.getNumberWaiting() < (cb.getParties() - reSetCount.intValue())) {
                    // waiting for all parties reach at barrier
                    // or all parties throws exception
                }
                // reset barrier
                cb.reset();
                System.out.println(Thread.currentThread() + " reset " + cb);
            } catch (InterruptedException | BrokenBarrierException ae) {
                reSetCount.incrementAndGet();
                // once barrier reset, other parties wait on barrier
                // will throw BrokenBarrierException
                System.out.println(Thread.currentThread() + " return by broken barrier.");
            } finally {
                // call shutdown hook method
                Thread.currentThread().interrupt();
                cb.reset();
            }
        }

        public static void main(String[] args) {
            reSetBarrierIf(13);
        }
    }
}
