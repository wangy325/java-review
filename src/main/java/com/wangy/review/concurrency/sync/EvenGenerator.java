package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 17:11
 */
public class EvenGenerator {

    /**
     * 抽象类提供模版方法
     */
    static abstract class AbstractIntGenerator {
        // volatile关键字意义不明显
        private volatile boolean canceled = false;

        /** 模版方法在子类中实现 */
        public abstract int next();

        /** 通用方法在抽象类中即可实现 */
        public void cancel() {
            // 基础类型的操作是原子的
            canceled = true;
        }

        public boolean isCanceled() {
            return canceled;
        }
    }

    /**
     * 具体类实现抽象模版的模版方法，因具体业务不同而差异
     */
    static class Generator extends AbstractIntGenerator {
        private int even = 0;

        @Override
        public int next() {
            ++even;  // danger here!
            ++even;
            return even;
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
                // next()可能被争用
                int next = evenGenerator.next();
                if (next % 2 != 0) {
                    // 这里读到的even值都是中间状态的值
                    System.out.println(Thread.currentThread().toString() + next + " not even!");
                    //
                    evenGenerator.cancel();
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("press Ctrl-C to exit");
        Generator evenGenerator = new Generator();
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 当线程数为1时，程序不会出现讹误
        for (int i = 0; i < 3; i++) {
            executorService.execute(new EvenTask(evenGenerator));
        }
        executorService.shutdown();
    }
}
