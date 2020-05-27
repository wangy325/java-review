package com.wangy.review.concurrency.sync;

import java.util.concurrent.TimeUnit;

/**
 * 线程之前的协作<br>
 * 假设播放之前必须录音，播放之后必须等待再次录音才能继续播放
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/23 / 14:20
 */
public class TrackPlay {

    /**
     * 2个任务，2个线程
     */
    private boolean tracked = false;

    synchronized void playTrack() throws InterruptedException {
        if (!tracked) {
            wait();
        }
        System.out.print("play ");
        tracked = false;
    }

    synchronized void recordTrack() {
        if (tracked) {
            return;
        }
        System.out.print("record ");
        tracked = true;
        notify();
    }

    public static void main(String[] args) throws InterruptedException {
        TrackPlay tp = new TrackPlay();
        Thread p = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        tp.playTrack();
                    }
                } catch (InterruptedException e) {
                    // ignore
                    System.out.println("exiting via interrupt");
                }
            }
        });
        Thread r = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        tp.recordTrack();
                    } catch (InterruptedException e) {
                        System.out.println("exiting via interrupt");
                    }
                }
            }
        });

        p.start();
        r.start();
        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }
}
