package com.wangy.review.concurrency.basic;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 使用<code>Thread.UncaughtExceptionHandler</code>处理run方法异常
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 11:12
 * @see Thread.UncaughtExceptionHandler
 */
@SuppressWarnings("all")
public class ExceptionThread {
    private Thread t = new Thread(new ExceptionT());

    private MyUncaughtExceptionHandler exceptionHandler = new MyUncaughtExceptionHandler(true);

    public static void main(String[] args) {
        ExceptionThread et = new ExceptionThread();
//        et.cannotCatch();
//        et.caughtThis();
        et.caughtAll();
    }

    /** 从run()方法抛出的异常不能被捕获 */
    void cannotCatch() {
        try {
            t.start();
        } catch (Exception x) {
            // 无法捕获从run()方法抛出的异常
            System.out.println(x.toString());
        }
        Thread.yield();
        System.out.println("[" + Thread.currentThread().getName() + "]:" + "can access");
    }

    /** 为线程指定异常处理器 */
    void caughtThis() {
        // 为当前线程指定异常处理器
        t.setUncaughtExceptionHandler(exceptionHandler);
        t.start();
        // 主线程不受影响
        System.out.println("[" + Thread.currentThread().getName() + "]:" + "is not affected");
    }

    void caughtAll() {
        // 为所有线程设置默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        t.start();
    }

    class ExceptionT implements Runnable {
        @Override
        public void run() {
            // run()方法不能抛出受查异常
            // throw new IOException(); // illegal
            throw new RuntimeException();
            // 异常抛出之后无法做任何事情了
        }
    }

    class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        private boolean isDeafult;

        public MyUncaughtExceptionHandler(boolean isDeafult) {
            this.isDeafult = isDeafult;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("[" + t.getName() + "]: " + "default ?(" + isDeafult + ") " + "caught " + e);
            //在线程结束之前还可以执行一些操作
            System.out.println("[" + t.getName() + "] state: " + t.getState());
//            e.printStackTrace();
        }
    }
}
