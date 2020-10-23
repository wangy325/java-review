package com.wangy.review.concurrency.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 利用线程中断<code>interrupt()</code>来终止任务
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/23 / 12:17
 */
public class Wax {

    static class Car {
        private boolean waxOn = true;

        public synchronized void waxed() {
            waxOn = true; // Ready to buff
            notifyAll();
        }

        public synchronized void buffed() {
            waxOn = false; // Ready for another coat of wax
            notifyAll();
        }

        public synchronized void waitForWaxing() throws InterruptedException {
            // waxOn = false时一直等待
            while (!waxOn)
                wait();
        }

        public synchronized void waitForBuffing() throws InterruptedException {
            // waxOn = true时一直等待
            while (waxOn)
                wait();

        }
    }

    static class WaxOn implements Runnable {
        private Car car;

        public WaxOn(Car c) {
            car = c;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
//                    TimeUnit.MILLISECONDS.sleep(100);
                    System.out.print("Wax On! ");
                    car.waxed();
                    car.waitForBuffing();
                }
            } catch (InterruptedException e) {
                System.out.println("WaxOn Exiting via interrupt");
            }
            System.out.println("Ending Wax On task");
        }
    }

    static class BufferOn implements Runnable {
        private Car car;

        public BufferOn(Car c) {
            car = c;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // 任务直接进入等待直到被唤醒, waxOn = true时得以执行
                    car.waitForWaxing();
                    System.out.print("Wax Off! ");
                    TimeUnit.MILLISECONDS.sleep(100);
                    car.buffed();
                }
            } catch (InterruptedException e) {
                System.out.println("BufferOn Exiting via interrupt");
            }
            System.out.println("Ending Buffer On task");
        }
    }

    public static void main(String[] args) throws Exception {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOn(car));
        exec.execute(new BufferOn(car));
        TimeUnit.SECONDS.sleep(2); // Run for a while...
        exec.shutdownNow(); // Interrupt all tasks
    }
}
/* Output: (95% match)
Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! BufferOn Exiting via interrupt
WaxOn Exiting via interrupt
Ending Wax On task
Ending Buffer On task
*///:~

