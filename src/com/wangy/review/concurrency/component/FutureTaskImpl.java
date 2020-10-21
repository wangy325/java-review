package com.wangy.review.concurrency.component;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/10/21 / 14:51
 */
public class FutureTaskImpl<V> extends FutureTask<V> {
    private int runTime = 0;
    private boolean isDone = false;


    public FutureTaskImpl(Callable<V> callable) {
        super(callable);
    }

    public FutureTaskImpl(Runnable runnable, V result) {
        super(runnable, result);
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            System.out.println("task is canceled");
            return;
        }
        isDone = true;
        runTime++;
    }

    @Override
    protected boolean runAndReset() {
        if (super.runAndReset()) {
            runTime++;
        } else {
            return false;
        }
        return true;
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            // do something
        }
    }

    static class Task2 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            return sum;
        }
    }

    /**
     * 先执行{@link FutureTask#run()}再执行{@link #runAndReset()}
     * <p>
     * 任务不可执行
     */
    void resetAfterRun() {
        run();
        System.out.println(runAndReset()); // false
        System.out.println("runTime:" + runTime);
        System.out.println("isDone:" + isDone);
    }

    /**
     * 先执行{@link #runAndReset()}再执行{@link FutureTask#run()}
     * <p>
     * 任务可以再次执行
     *
     * 对于有返回值的任务，执行{@link #runAndReset()}之后调用{@link FutureTask#get()}
     * 方法获取返回值会造成阻塞
     */
    @SneakyThrows
    void runAfterReset() {
        for (; ; ) {
            runAndReset();
            if (runTime > 1) break;
        }
//        V v = get(); // blocked
        System.out.println("isDone: " + isDone); // false
        run();
        System.out.println("runTime: " + runTime);
        V v1 = get();
        System.out.println("result: " + v1);
        System.out.println("isDone: " + isDone); // true
    }

    public static void main(String[] args) {
        // 构造一个没有返回值的FutureTask
        FutureTaskImpl<?> ft = new FutureTaskImpl<>(new Task(), null);
        FutureTaskImpl<?> ft2 = new FutureTaskImpl<>(new Task2());
        ft2.runAfterReset();
//        ft.resetAfterRun();
    }
}
