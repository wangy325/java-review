package com.wangy.review.concurrency.basic;

/**
 * join也可以被中断
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 01:12
 */
public class JoinAndSleep {
    public static void main(String[] args) {
        Slepper sa = new Slepper("sa", 100);
        Slepper sb = new Slepper("sb", 100);
        Joiner ja = new Joiner("ja", sa);
        Joiner jb = new Joiner("jb", sb);
//        sa.interrupt();
        ja.interrupt();
    }

    static class Slepper extends Thread {
        private int duration;

        public Slepper(String name, int duration) {
            super(name);
            this.duration = duration;
            start();
        }

        @Override
        public void run() {
            try {
                sleep(duration);
            } catch (InterruptedException e) {
                System.out.println(currentThread() + "is interrupted ? " + isInterrupted());
                return;
            }
            System.out.println(currentThread() + " has awakened.");
        }
    }

    static class Joiner extends Thread {
        private Slepper slepper;

        public Joiner(String name, Slepper slepper) {
            super(name);
            this.slepper = slepper;
            start();
        }

        @Override
        public void run() {
            try {
                slepper.join();
            } catch (InterruptedException e) {
                System.out.println(currentThread() + " interrupted()");
                return;
            }
            System.out.println(currentThread() + "join completed.");
        }
    }
}
