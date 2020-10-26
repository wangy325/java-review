package com.wangy.review.concurrency.component;

import com.wangy.review.concurrency.basic.LiftOff;
import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/10/25 / 23:46
 */
public class TestBlockingQueue {

    private BlockingQueue<LiftOff> rockets;

    private TestBlockingQueue(BlockingQueue<LiftOff> rockets) {
        this.rockets = rockets;
    }

    static TestBlockingQueue getLinkedBlockingQueue() {
        return new TestBlockingQueue(new LinkedBlockingQueue<>());
    }

    static TestBlockingQueue getArrayBlockedQueue(int capacity) {
        return new TestBlockingQueue(new ArrayBlockingQueue<>(capacity));
    }

    static TestBlockingQueue getSynchronousQueue() {
        return new TestBlockingQueue(new SynchronousQueue<>());
    }

    void add() throws InterruptedException {
        rockets.put(new LiftOff(1));
    }

    LiftOff take() throws InterruptedException {
        return rockets.take();
    }

    class LiftOffAdder implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    add();
                    Thread.yield();
                }
                System.out.println("Exiting LiftOffAdder");
            } catch (InterruptedException e) {
                System.out.println("Interrupted during add()");
            }
        }
    }

    class LiftOffRunner implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    LiftOff rocket = take();
                    // 在此线程上运行
                    rocket.run();
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted during sleep");
                        // return 语句是必须的，捕获异常后状态被清除了，while循环无法终止
                        return;
                    }
                }
                System.out.println("Exiting LiftOffRunner");
            } catch (InterruptedException e) {
                System.out.println("Interrupted during take()");
            }
        }
    }


    @SneakyThrows
    void test(String msg) {
        System.out.println(msg);
        ExecutorService pool = Executors.newCachedThreadPool();
        LiftOffRunner runner = this.new LiftOffRunner();
        LiftOffAdder adder = this.new LiftOffAdder();
        pool.execute(runner);
        pool.execute(adder);

        TimeUnit.SECONDS.sleep(1);
        pool.shutdownNow();
        System.out.println("rocket still in queue: " + rockets.size());
    }

    public static void main(String[] args) {
        getLinkedBlockingQueue().test("LinkedBlockingQueue");
        getArrayBlockedQueue(1).test("ArrayBlockingQueue");
        getSynchronousQueue().test("SynchronousQueue");
    }
}
/* (sample)
LinkedBlockingQueue
#0(LiftOff!),
#1(LiftOff!),
#2(LiftOff!),
#3(LiftOff!),
#4(LiftOff!),
#5(LiftOff!),
#6(LiftOff!),
#7(LiftOff!),
Exiting LiftOffAdder
rocket still in queue: 2087449
Interrupted during sleep
ArrayBlockingQueue
#2087457(LiftOff!),
#2087458(LiftOff!),
#2087459(LiftOff!),
#2087460(LiftOff!),
#2087461(LiftOff!),
#2087462(LiftOff!),
#2087463(LiftOff!),
#2087464(LiftOff!),
#2087465(LiftOff!),
#2087466(LiftOff!),
rocket still in queue: 1
Interrupted during sleep
Interrupted during add()
SynchronousQueue
#2087469(LiftOff!),
#2087470(LiftOff!),
#2087471(LiftOff!),
#2087472(LiftOff!),
#2087473(LiftOff!),
#2087474(LiftOff!),
#2087475(LiftOff!),
#2087476(LiftOff!),
#2087477(LiftOff!),
#2087478(LiftOff!),
rocket still in queue: 0
Interrupted during sleep
Interrupted during add()
 *///:~
