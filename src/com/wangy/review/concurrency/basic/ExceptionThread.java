package com.wangy.review.concurrency.basic;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 11:12
 */
public class ExceptionThread {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(true));
        Thread t = new Thread(new ExceptionT());
        // condition 1
        /*try {
            t.start();
        } catch (Exception x) {
            x.printStackTrace();
        }*/
        //condition 2
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        t.start();
        Thread t1 = new Thread(new ExceptionT());
        t1.start();

    }

    static class ExceptionT implements Runnable {
        @Override
        public void run() {
            throw new RuntimeException();
        }
    }

    static class MyUncaughtExceptionHandler
        implements Thread.UncaughtExceptionHandler {

        private boolean isDeafult;

        public MyUncaughtExceptionHandler() {
        }

        public MyUncaughtExceptionHandler(boolean isDeafult) {
            this.isDeafult = isDeafult;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("default ?(" + isDeafult+ ") " + "caught " + e);
        }
    }
}
