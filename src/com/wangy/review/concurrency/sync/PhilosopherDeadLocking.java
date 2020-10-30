package com.wangy.review.concurrency.sync;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 哲学家吃饭都成问题
 *
 * 循环等待造成的死锁问题
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/29 / 15:41
 */
public class PhilosopherDeadLocking {

    protected final int id;
    protected final int ponderFactor;

    public PhilosopherDeadLocking(int id, int ponderFactor) {
        this.id = id;
        this.ponderFactor = ponderFactor;
    }

    protected void pause() throws InterruptedException {
        Random rand = new Random(47);
        if (ponderFactor == 0) {
            return;
        }
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
    }


    @Override
    public String toString() {
        return "Philosopher " + id;
    }

    static class Chopstick {
        private boolean taken;

        private synchronized void take() throws InterruptedException {
            while (taken) {
                wait();
            }
            taken = true;
        }

        private synchronized void drop() {
            taken = false;
            notifyAll();
        }
    }

    static class Dinner implements Runnable {
        private Chopstick left;
        private Chopstick right;
        private PhilosopherDeadLocking philosopherDeadLocking;


        public Dinner(Chopstick left, Chopstick right, PhilosopherDeadLocking phi) {
            this.left = left;
            this.right = right;
            this.philosopherDeadLocking = phi;
        }


        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    System.out.println(philosopherDeadLocking + " " + "thinking");
                    philosopherDeadLocking.pause();
                    // Philosopher becomes hungry
                    System.out.println(philosopherDeadLocking + " " + "grabbing right");
                    right.take();
                    System.out.println(philosopherDeadLocking + " " + "grabbing left");
                    left.take();
                    System.out.println(philosopherDeadLocking + " " + "eating");
                    philosopherDeadLocking.pause();
                    System.out.println(philosopherDeadLocking + " " + "drop right");
                    right.drop();
                    System.out.println(philosopherDeadLocking + " " + "drop left");
                    left.drop();
                }
            } catch (InterruptedException e) {
                System.out.println(philosopherDeadLocking + " " + "exiting via interrupt");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        int size = 5, ponder = 0;
        if (args.length > 0) {
            ponder = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            size = Integer.parseInt(args[1]);
        }
        Chopstick[] chopsticks = new Chopstick[size];

        for (int i = 0; i < size; i++) {
            chopsticks[i] = new Chopstick();
        }
        for (int i = 0; i < size; i++) {
            pool.execute(new Dinner(chopsticks[i], chopsticks[(i + 1) % size], new PhilosopherDeadLocking(i, ponder)));
        }

        if (args.length > 2) {
            TimeUnit.SECONDS.sleep(Integer.parseInt(args[2]));
        } else {
            System.out.println("Press 'q' to quit");
            System.in.read();
        }
        pool.shutdownNow();
    }
}
