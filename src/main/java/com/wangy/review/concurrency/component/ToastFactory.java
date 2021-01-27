package com.wangy.review.concurrency.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列构成的阻塞链
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/26 / 18:46
 */
public class ToastFactory {
    private volatile int count;

    static class Toast {
        enum Status {DRY, BUTTERED, JAMMED}

        private Status status = Status.DRY;
        private final int id;

        public Toast(int idn) {
            id = idn;
        }

        public void butter() {
            status = Status.BUTTERED;
        }

        public void jam() {
            status = Status.JAMMED;
        }

        public Status getStatus() {
            return status;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Toast " + id + ": " + status;
        }
    }

    class ToastQueue extends LinkedBlockingQueue<Toast> {
    }

    class Toaster implements Runnable {
        private ToastQueue rawQueue;


        public Toaster(ToastQueue tq) {
            rawQueue = tq;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // 这句休眠是保证阻塞链的根本
                    TimeUnit.NANOSECONDS.sleep(1);
                    // Make toast
                    Toast t = new Toast(count++);
                    System.out.println(t);
                    // Insert into queue
                    rawQueue.put(t);
                }
                System.out.println("Toaster off");
            } catch (InterruptedException e) {
                System.out.println("Toaster interrupted");
            }
        }
    }

    /** Apply butter to toast: */
    class Butterer implements Runnable {
        private ToastQueue dryQueue, finishQueue;

        public Butterer(ToastQueue dry, ToastQueue buttered) {
            dryQueue = dry;
            finishQueue = buttered;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // Blocks until next piece of toast is available:
                    Toast t = dryQueue.take();
                    t.butter();
                    System.out.println(t);
                    finishQueue.put(t);
                }
                System.out.println("Butterer off");
            } catch (InterruptedException e) {
                System.out.println("Butterer interrupted");
            }
        }
    }

    /** Apply jam to buttered toast: */
    class Jammer implements Runnable {
        private ToastQueue dryQueue, finishQueue;

        public Jammer(ToastQueue raw, ToastQueue jam) {
            dryQueue = raw;
            finishQueue = jam;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // Blocks until next piece of toast is available:
                    Toast t = dryQueue.take();
                    t.jam();
                    System.out.println(t);
                    finishQueue.put(t);
                }
                System.out.println("Jammer off");
            } catch (InterruptedException e) {
                System.out.println("Jammer interrupted");
            }
        }
    }

    /** Consume the toast: */
    class Eater implements Runnable {
        private ToastQueue finishQueue;
        private int counter = 0;

        public Eater(ToastQueue finishQueue) {
            this.finishQueue = finishQueue;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // Blocks until next piece of toast is available:
                    Toast toast = finishQueue.take();
                    // Verify that the toast is coming in order,
                    // and that all pieces are getting jammed:
                    if (toast.getId() != counter++ || toast.getStatus() == Toast.Status.DRY) {
                        System.out.println(">>>> Error: " + toast);
                        System.exit(1);
                    } else {
                        System.out.println("Chomp! " + toast);
                    }
                }
                System.out.println("Eater off");
            } catch (InterruptedException e) {
                System.out.println("Eater interrupted");
            }
        }
    }


    public void test() throws InterruptedException {
        ToastQueue dryQueue = this.new ToastQueue(),
            finishQueue = this.new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(this.new Toaster(dryQueue));
        exec.execute(this.new Butterer(dryQueue, finishQueue));
        exec.execute(this.new Jammer(dryQueue, finishQueue));
        exec.execute(this.new Eater(finishQueue));

        while (true) {
            if (count > 40) {
                break;
            }
        }
        exec.shutdownNow();
        System.out.println("toast count: " + count);
    }

    public static void main(String[] args) throws Exception {
        ToastFactory tf = new ToastFactory();
        tf.test();
    }

}/*
Toast 0: DRY
Toast 0: BUTTERED
Chomp! Toast 0: BUTTERED
Toast 1: DRY
Toast 1: JAMMED
Chomp! Toast 1: JAMMED
Toast 2: DRY
Toast 2: BUTTERED
Chomp! Toast 2: BUTTERED
Toast 3: DRY
Toast 3: JAMMED
Chomp! Toast 3: JAMMED
Toast 4: DRY
Toast 4: BUTTERED
Chomp! Toast 4: BUTTERED
toast count: 5
Eater interrupted
Jammer interrupted
Butterer interrupted
Toaster interrupted
 *///:~
