package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 23:58
 */
public class EvenGeneratorWithIntrinsicLock {
    public static void main(String[] args) {
        System.out.println("press Ctrl-C to exit");
        Generator evenGenerator = new Generator();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Thread(new EvenTask(evenGenerator)));
        }
        executorService.shutdown();
    }



    static class Generator extends EvenGenerator.AbstractIntGenerator {
        private Integer even = 0;
        private final Object lock = new Object();

        @Override
        public int next() {
//             synchronized (this){
            synchronized (lock) {
                ++even;
                ++even;
                // return语句必须包含在同步代码块里
                return even;
            }
        }
    }

    static class EvenTask implements Runnable {
        private final Generator evenGenerator;

        public EvenTask(Generator evenGenerator) {
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
