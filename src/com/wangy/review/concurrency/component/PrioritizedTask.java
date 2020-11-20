package com.wangy.review.concurrency.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 够造一个可以放入优先级队列的任务
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/20 / 20:18
 */
public class PrioritizedTask implements Runnable, Comparable<PrioritizedTask> {
    private Random rand = new Random(47);
    private static int counter = 0;
    private final int id = counter++;
    private final int priority;
    protected static List<PrioritizedTask> sequence = new ArrayList<>();

    public PrioritizedTask(int priority) {
        this.priority = priority;
        sequence.add(this);
    }

    @Override
    public int compareTo(PrioritizedTask arg) {
        return priority < arg.priority ? 1 :
            (priority > arg.priority ? -1 : 0);
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(250));
        } catch (InterruptedException e) {
            // Acceptable way to exit
        }
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("[%1$-3d]", priority) +
            " Task " + id;
    }

    public String summary() {
        return "(" + id + ":" + priority + ")";
    }

    static class EndSentinel extends PrioritizedTask {
        private ExecutorService exec;

        public EndSentinel(ExecutorService e) {
            super(-1); // Lowest priority in this program
            exec = e;
        }

        @Override
        public void run() {
            int count = 0;
            for (PrioritizedTask pt : sequence) {
                System.out.print(pt.summary());
                if (++count % 5 == 0)
                    System.out.println();
            }
            System.out.println();
            System.out.println(this + " Calling shutdownNow()");
            exec.shutdownNow();
        }
    }
}
