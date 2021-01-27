package com.wangy.review.concurrency.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@link Wax}的兄弟版本
 *
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
            while (!Thread.interrupted()) {
                synchronized (rest) {
                    if (rest.meal != null) {
                        try {
                            rest.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Exit Chef by Interrupted");
                            return;
                        }
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Exit Chef Sleep by Interrupted");
                        return;
                    }
                    rest.meal = new Meal(++rest.count);
                    rest.notifyAll();
                }
            }
            System.out.println("Exit Chef");
        }
    }

    static class Waiter implements Runnable {

        final Restaurant rest;

        public Waiter(Restaurant rest) {
            this.rest = rest;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                synchronized (rest) {
                    if (rest.meal == null) {
                        try {
                            rest.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Exit Waiter by Interrupted");
                            return;
                        }
                    }
                    System.out.println("order up: " + rest.meal);
                    rest.meal = null;
                    rest.notifyAll();
                }
            }
            System.out.println("Exit Waiter");
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        Restaurant restaurant = new Restaurant();
        pool.execute(new Waiter(restaurant));
        pool.execute(new Chef(restaurant));

        while (true) {
            synchronized (restaurant) {
                if (restaurant.count == 10) {
                    pool.shutdownNow();
                    break;
                }
            }
        }
        // end
    }
}
/*（sample）
order up: Meal 1
order up: Meal 2
order up: Meal 3
order up: Meal 4
order up: Meal 5
order up: Meal 6
order up: Meal 7
order up: Meal 8
order up: Meal 9
order up: Meal 10
exit waiter by interrupted
Exit Chef
 *///:~
