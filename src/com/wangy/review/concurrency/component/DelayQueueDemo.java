package com.wangy.review.concurrency.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * simple usage of delayQueue
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/20 / 22:04
 */
public class DelayQueueDemo {

    private static class DelayedTask implements Runnable, Delayed {
        protected static List<DelayedTask> sequence = new ArrayList<>();
        private static int counter = 0;
        private final int id = counter++;
        private final int delta;
        /** 到期时间 */
        private final long trigger;

        public DelayedTask(int delayInMilliseconds) {
            delta = delayInMilliseconds;
            trigger = System.nanoTime() + NANOSECONDS.convert(delta, MILLISECONDS);
            sequence.add(this);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(trigger - System.nanoTime(), NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed arg) {
            DelayedTask that = (DelayedTask) arg;
            if (trigger < that.trigger) return -1;
            if (trigger > that.trigger) return 1;
            return 0;
        }

        @Override
        public void run() {
            System.out.print(this + " ");
        }

        @Override
        public String toString() {
            return String.format("[%1$-4d]", delta) + " Task " + id;
        }

        public String summary() {
            return "(" + id + ":" + delta + ")";
        }

        static class EndSentinel extends DelayedTask {
            private ExecutorService exec;

            public EndSentinel(int delay, ExecutorService e) {
                super(delay);
                exec = e;
            }

            @Override
            public void run() {
                System.out.println();
                for (DelayedTask pt : sequence) {
                    System.out.print(pt.summary() + " ");
                }
                System.out.println();
                System.out.println(this + " Calling shutdownNow()");
                exec.shutdownNow();
            }
        }
    }

    static class DelayedTaskConsumer implements Runnable {
        private DelayQueue<DelayedTask> q;

        public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
            this.q = q;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted())
                    // Run task with the current thread
                    q.take().run();
            } catch (InterruptedException e) {
                // Acceptable way to exit
            }
            System.out.println("Finished DelayedTaskConsumer");
        }
    }

    public static void main(String[] args) {
        Random rand = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        // Fill with tasks that have random delays:
        for (int i = 0; i < 20; i++)
            queue.put(new DelayedTask(rand.nextInt(5000)));
        // Set the stopping point
        queue.add(new DelayedTask.EndSentinel(5000, exec));
        exec.execute(new DelayedTaskConsumer(queue));
    }
}
