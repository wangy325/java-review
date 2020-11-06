package com.wangy.review.concurrency.component;

import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 如何关闭线程池<br>
 * 关于执行器的状态，参考{@link ThreadPoolExecutor}中关于运行状态的描述：
 * <pre>
 *      The runState provides the main lifecycle control, taking on values:
 *
 *         RUNNING:  Accept new tasks and process queued tasks
 *         SHUTDOWN: Don't accept new tasks, but process queued tasks
 *         STOP:     Don't accept new tasks, don't process queued tasks,
 *                   and interrupt in-progress tasks
 *         TIDYING:  All tasks have terminated, workerCount is zero,
 *                   the thread transitioning to state TIDYING
 *                   will run the terminated() hook method
 *         TERMINATED: terminated() has completed
 *
 * </pre>
 * 其中，{@link ThreadPoolExecutor#shutdown()}方法对应shutdown，
 * {@link ThreadPoolExecutor#shutdownNow()}对应stop
 * 可以看到，shutdown方法和shutdownNow方法的差别很明显了，值得一提的是，
 * 后者虽然尝试停止线程，但是如果正在执行的任务不响应interrupt方法，
 * 那么线程也无法完全terminate(终止线程的terminate()方法无法调用)，
 * 正如shutdownNow方法文档里描述的那样：
 * <pre>
 * This implementation cancels tasks via {@link Thread#interrupt},
 * so any task that fails to respond to interrupts may never terminate.
 * </pre>
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/11 / 22:26
 */
public class ExecutorShutdown {

    static int pointer = 0;
    /** 容量为1的线程池，其能保证提交的任务都是序列化执行的 */
    ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    @SneakyThrows
    public static void main(String[] args) {
        ExecutorShutdown es = new ExecutorShutdown();
//        es.shutdown();
        es.awaitTermination(1, TimeUnit.SECONDS);
        /*Future<Double> submit = es.service.submit(new Cal());
        es.service.shutdownNow();
        // future可以判断任务的执行进度
        if (submit.isDone()) {
            System.out.println(submit.get());
        }
        System.out.println(submit.get());*/
    }

    /**
     * <p>shutdown: 在线程池队列中的提交的任务会执行，无法提交新的任务。</br>
     * 注意调用这个方法，线程池不会等待（wait）在执行的任务执行完成（complete execution），可以使用awaitTermination实现这个目的。<br>
     * 需要注意的是，在执行的任务因为是异步线程执行的，<b>任务还是会继续执行</b>
     */
    void shutdown() {
        service.execute(new ComplexTask());
        // 对于newFixedThreadPool(1),EasyTask在任务队列中
        service.execute(new EasyTask());
        service.shutdown();
        // shutdown之后，任务并没有执行完成，pointer的值还是0
        System.out.println("pointer:" + pointer);

        // 获取待任务队列
        System.out.println("workQueue: " + service.getQueue());
        // 判断该执行器是否被关闭
        System.out.println("is executor shutdown? " + service.isShutdown());
        // 执行器关闭之后所有任务是否都完成
        // 如果没有调用shutdown()或shutdownNow()就直接调用isTerminated()，该方法必返回false
        System.out.println("is executor terminated? " + service.isTerminated());
        System.out.println("pointer:" + pointer);
    }

    /**
     * <p>awaitTermination: 阻塞当前线程直到：
     * <ol>
     *     <li>shutdown()之后所有任务执行完成</li>
     *     <li>超时</li>
     *     <li>当前线程被中断</li>
     * </ol>
     * <br>超时之后，活动任务和队列中的任务仍然会异步执行完毕
     * <b>这个方法不会关闭线程池，不要单独使用这个方法！</b>
     * </p>
     */
    void awaitTermination(int timeout, TimeUnit unit) {
        service.execute(new ComplexTask());
        service.execute(new EasyTask());
        List<Runnable> tasks;
        try {
            if (service.awaitTermination(timeout, unit)) {
                service.shutdown();
            } else {
                if(!(tasks = service.shutdownNow()).isEmpty()){
                    System.out.println("丢弃任务" + tasks);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("workQueue: " + service.getQueue());
        System.out.println("is executor shutdown? " + service.isShutdown());
        System.out.println("is executor terminated? " + service.isTerminated());
    }

    abstract class Task {
        @Override
        public String toString() {
            return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
        }
    }

    class ComplexTask extends Task implements Runnable {
        @Override
        public void run() {
            // 响应中断，调用shutdownNow()可以结束任务
            System.out.println("[" + Thread.currentThread() + "@" + this + "]，开始执行");
            // never finish unless interrupted
            for (; ; ) {
                if (!Thread.interrupted()) {
                    pointer++;
                } else {
                    System.out.println("[" + Thread.currentThread() + "@" + this + "]，被中断");
                    break;
                }
            }
        }
    }

    class EasyTask extends Task implements Runnable {
        @Override
        public void run() {
            System.out.println("[" + Thread.currentThread() + "@" + this + "]，开始执行");
            pointer++;
            System.out.println("[" + Thread.currentThread() + "@" + this + "]，执行完成");
        }
    }


    static class Cal implements Callable<Double> {

        @Override
        public Double call() throws Exception {
            double d = Math.PI;
            for (int i = 0; i < 3000000; i++) {
                d = d + Math.PI / Math.E;
            }
            return d;
        }
    }
}
