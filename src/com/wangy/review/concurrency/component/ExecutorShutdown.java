package com.wangy.review.concurrency.component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 如何关闭线程池
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/11 / 22:26
 */
public class ExecutorShutdown {

    static int pointer = 0;
    /** 容量为1的线程池，其能保证提交的任务都是序列化执行的 */
    ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);


    public static void main(String[] args) {
        ExecutorShutdown es = new ExecutorShutdown();
//        es.shutdown();
//        es.shutdownNow();
        es.awaitTermination();
    }

    /**
     * <p>shutdown: 在线程池队列中的提交的任务会执行，无法提交新的任务。</br>
     * 注意调用这个方法，线程池不会等待（wait）在执行的任务执行完成（complete execution），可以使用awaitTermination实现这个目的。<br>
     * 需要注意的是，在执行的任务因为是异步线程执行的，<b>任务还是会继续执行</b>，只是说线程池不会阻塞等待任务执行完成</p>
     */
    void shutdown() {
        service.execute(new ComplexTask());
        // 对于newFixedThreadPool(1),EasyTask在任务队列中
        service.execute(new EasyTask());
        service.shutdown();
        // shutdown之后，任务并没有执行完成，pointer的值还是0
        System.out.println(pointer);

        // 获取待任务队列
        System.out.println("task queue: " + service.getQueue());
        // 判断该执行器是否被关闭
        System.out.println("is executor shutdown? " + service.isShutdown());
        // 执行器关闭之后所有任务是否都完成
        // 如果没有调用shutdown()或shutdownNow()就直接调用isTerminated()，该方法必返回false
        System.out.println("is executor terminated? " + service.isTerminated());
    }

    /**
     * <p>shutdownNow: 尝试关闭所有正在执行的任务，不执行队列中的任务，返回队列中等待执行的任务列表<br>
     * 此方法不会等待正在执行的任务执行完（to terminate）
     * </p>
     */
    void shutdownNow() {
        service.execute(new ComplexTask());
        service.execute(new EasyTask());
        // 返回队列中等待执行的任务
        List<Runnable> tasks = service.shutdownNow();
        System.out.println("submitted but will discarded tasks: " + tasks);
        System.out.println("task queue: " + service.getQueue());
        System.out.println("is executor shutdown? " + service.isShutdown());
        System.out.println("is executor terminated? " + service.isTerminated());
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
     *
     */
    void awaitTermination() {
        service.execute(new ComplexTask());
        service.execute(new EasyTask());
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task queue: " + service.getQueue());
        System.out.println("is executor shutdown? " + service.isShutdown());
        System.out.println("is executor terminated? " + service.isTerminated());
    }

    static class ComplexTask implements Runnable {
        @Override
        public void run() {
            //这里模拟的是执行时间较长的任务
            try {
                System.out.println("[" + Thread.currentThread() + "@" + this + "]，开始执行");
                TimeUnit.SECONDS.sleep(5);
                pointer++;
                System.out.println("[" + Thread.currentThread() + "@" + this + "]，执行完成");
            } catch (InterruptedException e) {
                System.out.println("[" + Thread.currentThread() + "@" + this + "]，被中断");
            }
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
        }
    }

    static class EasyTask implements Runnable {
        @Override
        public void run() {
            System.out.println("[" + Thread.currentThread() + "@" + this + "]，开始执行");
            pointer++;
            System.out.println("[" + Thread.currentThread() + "@" + this + "]，执行完成");
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
        }
    }


}
