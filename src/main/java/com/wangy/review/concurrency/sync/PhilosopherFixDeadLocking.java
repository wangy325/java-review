package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 破坏循环等待条件即可破坏死锁
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/30 / 21:40
 */
public class PhilosopherFixDeadLocking extends PhilosopherDeadLocking {
    public PhilosopherFixDeadLocking(int id, int ponderFactor) {
        super(id, ponderFactor);
    }

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        int size = 5, ponder = 0;
        if (args.length > 0) {
            ponder = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            size = Integer.parseInt(args[1]);
        }
        Chopstick[] chopsticks = new Chopstick[size];

        for (int i = 0; i < size; i++) {
            chopsticks[i] = new Chopstick();
        }
        for (int i = 0; i < size - 1; i++) {
            pool.execute(new Dinner(chopsticks[i], chopsticks[(i + 1) % size], new PhilosopherDeadLocking(i, ponder)));
        }

        // 让最后一位哲学家先拿左边的筷子，破坏可能发生的循环等待
        pool.execute(new Dinner(chopsticks[0],chopsticks[size -1], new PhilosopherFixDeadLocking(size-1, ponder)));

        if (args.length > 2) {
            TimeUnit.MILLISECONDS.sleep(Integer.parseInt(args[2]));
        } else {
            System.out.println("Press 'q' to quit");
            System.in.read();
        }
        pool.shutdownNow();
    }
}
