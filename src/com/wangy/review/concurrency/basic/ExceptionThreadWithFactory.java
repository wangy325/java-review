package com.wangy.review.concurrency.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 使用线程工厂+执行器创建线程并处理异常
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 14:41
 */
public class ExceptionThreadWithFactory {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool(new MyThreadFactory());
        executorService.execute(new ExceptionThread());
        executorService.shutdown();
    }


    static class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("caught " + e);
        }
    }

    static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // you can set up thread's infos here
            t.setUncaughtExceptionHandler(new MyExceptionHandler());
            return t;
        }
    }

    static class ExceptionThread implements Runnable {

        @Override
        public void run() {
            throw new RuntimeException();
        }
    }

}
