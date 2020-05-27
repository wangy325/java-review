package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 17:11
 */
public class UnSynchronizedEvenGenerator {
    public static void main(String[] args) {
        System.out.println("press Ctrl-C to exit");
        EvenGenerator evenGenerator = new EvenGenerator();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
           executorService.execute(new Thread(new EvenTask(evenGenerator)));
        }
        executorService.shutdown();
    }

    static abstract class AbstractIntGenerator {
        private volatile boolean canceled = false;

        public abstract int next();

        public void cancel() {
            canceled = true;
        }

        public boolean isCanceled() {
            return canceled;
        }
    }


    static class EvenGenerator extends AbstractIntGenerator {
        private int even = 0;

        @Override
        public int next() {
            ++even;  // danger here!
            ++even;
            return even;
        }
    }

    static class EvenTask implements Runnable {
        private EvenGenerator evenGenerator;

        public EvenTask(EvenGenerator evenGenerator) {
            this.evenGenerator = evenGenerator;
        }

        @Override
        public void run() {
            while (!evenGenerator.isCanceled()) {
                int next = evenGenerator.next();
                if (next % 2 != 0) {
                    System.out.println(Thread.currentThread().toString() + next + " not even!");
                    evenGenerator.cancel();
                }
            }
        }
    }
}
