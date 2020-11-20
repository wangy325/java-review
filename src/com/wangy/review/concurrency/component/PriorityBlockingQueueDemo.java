package com.wangy.review.concurrency.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/20 / 16:07
 */
public class PriorityBlockingQueueDemo {

    static class PrioritizedTaskProducer implements Runnable {
        private Random rand = new Random(47);
        private Queue<Runnable> queue;
        private ExecutorService exec;

        public PrioritizedTaskProducer(
            Queue<Runnable> q, ExecutorService e) {
            queue = q;
            exec = e; // Used for EndSentinel
        }

        @Override
        public void run() {
            // Unbounded queue; never blocks.
            // Fill it up fast with random priorities:
            for (int i = 0; i < 20; i++) {
                queue.add(new PrioritizedTask(rand.nextInt(10)));
                Thread.yield();
            }
            // Trickle in highest-priority jobs:
            try {
                for (int i = 0; i < 10; i++) {
                    TimeUnit.MILLISECONDS.sleep(250);
                    queue.add(new PrioritizedTask(10));
                }
                // Add jobs, lowest priority first:
                for (int i = 0; i < 10; i++)
                    queue.add(new PrioritizedTask(i));
                // A sentinel to stop all the tasks:
                queue.add(new PrioritizedTask.EndSentinel(exec));
            } catch (InterruptedException e) {
                // Acceptable way to exit
            }
            System.out.println("Finished PrioritizedTaskProducer");
        }
    }

    static class PrioritizedTaskConsumer implements Runnable {
        private PriorityBlockingQueue<Runnable> q;

        public PrioritizedTaskConsumer(
            PriorityBlockingQueue<Runnable> q) {
            this.q = q;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted())
                    // Use current thread to run the task:
                    q.take().run();
            } catch (InterruptedException e) {
                // Acceptable way to exit
            }
            System.out.println("Finished PrioritizedTaskConsumer");
        }
    }

    public static void main(String[] args) throws Exception {
        Random rand = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<>();
        exec.execute(new PrioritizedTaskProducer(queue, exec));
        exec.execute(new PrioritizedTaskConsumer(queue));
    }
}
