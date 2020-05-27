package com.wangy.review.concurrency.basic;

import java.util.concurrent.TimeUnit;

/**
 * sleep 休眠可以被阻塞
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/11 / 14:44
 */
public class InterruptAndSleep {
    public static void main(String[] args) {
        Thread apple = new Thread(new InnerThread(), "apple");
//        Thread google = new Thread(new InnerThread(), "google");
        apple.start();
//        google.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(apple.getState());
        apple.interrupt();
    }

    static class InnerThread implements Runnable {
        private static int count = 0;
        private final int id = count++;
        private int countDown = 3;

        public InnerThread() {
        }

        public void info() {
            System.out.println("id(" + id + " " + Thread.currentThread() + ") ");
        }

        @Override
        public void run() {
            /*try {
                while (countDown-- > 0) {
                    // Thread.sleep(100);
                    // Java SE5 or later style
                    TimeUnit.MILLISECONDS.sleep(100);
                    info();
                }
            } catch (InterruptedException e) {
                // e.printStackTrace();
                System.out.println("id(" + id + " " + Thread.currentThread() + ") is" + " interrupted");
            }*/

            // try this
            while (countDown-- > 0) {
                try {
                    // Thread.sleep(100);
                    // Java SE5 or later style
                    TimeUnit.MILLISECONDS.sleep(100);
                    info();
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                    System.out.println("id(" + id + " " + Thread.currentThread() + ") is" + " interrupted");
                }
            }
        }
    }
}
