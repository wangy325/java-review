package com.wangy.review.concurrency.component;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 多线程分部计算
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/20 / 18:42
 */
public class DividedCalculate {

    static class Task implements Callable<Integer> {

        int min;
        int max;

        public Task(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public Integer call() {
            int sum = 0;
            for (int i = min; i < max; i++) {
                sum += i;
            }
            return sum;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        Future<Integer> s3 = pool.submit(new Task(51, 76));
        Future<Integer> s2 = pool.submit(new Task(26, 51));
        Future<Integer> s4 = pool.submit(new Task(76, 101));
        Future<Integer> s1 = pool.submit(new Task(1, 26));

        pool.shutdown();
        System.out.printf("%d + %d + %d + %d = %d", s1.get(), s2.get(), s3.get(), s4.get(), s1.get() + s2.get() + s3.get() + s4.get());
    }

}
