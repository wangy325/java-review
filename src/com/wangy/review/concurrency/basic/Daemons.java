package com.wangy.review.concurrency.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/11 / 23:50
 */
public class Daemons {
    public static void main(String[] args) {
        Thread t =new Thread(new Daemon());
        t.setDaemon(true);
        t.start();
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //main线程结束，守护线程随即终止运行
    }

    static class Daemon implements Runnable {
        private List<Thread> threads = new ArrayList<>();

        @Override
        public void run() {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                threads.add(i, new Thread(new ThreadSpawn()));
                threads.get(i).start();
                System.out.println("thread["+i+"].isDaemon: " + threads.get(i).isDaemon());
            }
//            while (true) Thread.yield();
        }
    }

    static class ThreadSpawn implements Runnable {
        @Override
        public void run() {
            Thread.yield();
        }
    }
}
