package com.wangy.review.concurrency.sync;

import lombok.SneakyThrows;
import lombok.var;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 在监视器上等待<br>
 * 假设播放之前必须录音，播放之后必须等待再次录音才能继续播放
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/23 / 14:20
 */
public class WaitOnCondition {

    /**
     * 2个任务，2个线程
     */
    private boolean tracked = false;

    synchronized void playTrack() throws InterruptedException {
        if (!tracked) {
            wait();
        }
        System.out.println("play ");
        tracked = false;
    }

    synchronized void recordTrack() {
        if (tracked) {
            return;
        }
        System.out.println("record ");
        tracked = true;
        // 最好不要使用notify,除非你明确地知道期待的线程一定被唤醒
        notifyAll();
    }

    class Play implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                playTrack();
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        }
    }

    class Record implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                recordTrack();
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitOnCondition tp = new WaitOnCondition();
        var pool = Executors.newCachedThreadPool();
        pool.submit(tp.new Play());
        pool.submit((tp.new Record()));

        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }
}
