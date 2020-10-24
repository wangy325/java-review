package com.wangy.review.concurrency.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/10/24 / 17:28
 */
public class Restaurant {
    private Meal meal;
    private volatile int count;


    static class Meal {
        int orderNum;

        public Meal(int orderNum) {
            this.orderNum = orderNum;
        }

        @Override
        public String toString() {
            return "Meal " + orderNum;
        }
    }

    static class Chef implements Runnable {

        final Restaurant rest;

        public Chef(Restaurant rest) {
            this.rest = rest;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (rest) {
                        if (rest.meal != null) {
                            rest.wait();
                        }
//                        TimeUnit.MILLISECONDS.sleep(100);
                        rest.meal = new Meal(++rest.count);
                        rest.notifyAll();
                    }
                }
                System.out.println("Exit Chef");
            } catch (InterruptedException e) {
                System.out.println("exit chef by interrupted");
            }
        }
    }

    static class Waiter implements Runnable {

        final Restaurant rest;

        public Waiter(Restaurant rest) {
            this.rest = rest;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (rest) {
                        if (rest.meal == null) {
                            rest.wait();
                        }
                        System.out.println("order up: " + rest.meal);
                        rest.meal = null;
                        rest.notifyAll();
                    }
                }
                System.out.println("Exit Waiter");
            } catch (InterruptedException e) {
                System.out.println("exit waiter by interrupted");
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        Restaurant restaurant = new Restaurant();
        pool.execute(new Waiter(restaurant));
        pool.execute(new Chef(restaurant));

        while (true) {
            if (restaurant.count == 10) {
                pool.shutdownNow();
                break;
            }
        }
        // end
    }
}
