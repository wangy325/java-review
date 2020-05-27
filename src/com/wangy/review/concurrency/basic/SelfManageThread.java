package com.wangy.review.concurrency.basic;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 09:12
 */
public class SelfManageThread {
    Thread thread;
    public SelfManageThread() {
        thread= new Thread(new Runnable() {
            private int count = 5;

            @Override
            public String toString() {
                return "#" + Thread.currentThread().getName() + "(" + count + "), ";
            }

            @Override
            public void run() {
                while (--count > 0) {
                    System.out.print(this);
                    Thread.yield();
                }
            }
        });
        thread.start();
    }



    public static void main(String[] args) {
        for (int i = 0; i <5 ; i++) {

//            new SelfManaged();
            new SlefRunnable();

        }
    }

    static class SelfManaged extends Thread {
        private static int count = 0;
        private final int id = count;

        public SelfManaged() {
            super(String.valueOf(++count));
            start();
        }

        @Override
        public String toString() {
            return "#" + getName() + "(" + id + "), ";
        }

        @Override
        public void run() {
            System.out.print(this);
            Thread.yield();
        }
    }

    static class SlefRunnable implements Runnable{
        private static int count = 0;
        private final int id = ++count;
        private Thread t = new Thread(this, String.valueOf(id));

        public SlefRunnable() {
            t.start();
        }

        @Override
        public String toString() {
            return "#" + t.getName() + "(" + id + "), ";
        }

        @Override
        public void run() {
            System.out.print(this);
            Thread.yield();
        }
    }
}
