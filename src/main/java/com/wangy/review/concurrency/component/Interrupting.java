package com.wangy.review.concurrency.component;
// Interrupting a blocked thread.

import java.io.InputStream;
import java.util.concurrent.*;

/**
 * 从输出来看，似乎I/O阻塞以及同步阻塞的线程无法通过{@link Thread#interrupt()}方法中断，具体可以查看该方法的Javadoc
 * <p>
 * 但是，既然无法中断，那么为什么{@link Future#cancel(boolean)}方法返回却是true？
 * <p>
 * 看起来，{@link FutureTask#cancel(boolean)}方法调用<code>interrupt()</code>之后并没有额外的判断，而是直接返回了true
 * <p>
 * 那么此例中出现的结果和<code>cancel(boolean)</code>方法的关系不大
 * <p>
 * 看起来是阻塞的线程没有响应中断
 *
 * <pre>
 *     if (!(state == NEW && STATE.compareAndSet
 *               (this, NEW, mayInterruptIfRunning ? INTERRUPTING : CANCELLED)))
 *             return false;
 *         try {    // in case call to interrupt throws exception
 *             if (mayInterruptIfRunning) {
 *                 try {
 *                     Thread t = runner;
 *                     if (t != null)
 *                         t.interrupt();
 *                 } finally { // final state
 *                     STATE.setRelease(this, INTERRUPTED);
 *                 }
 *             }
 *         } finally {
 *             finishCompletion();
 *         }
 *         return true;
 * </pre>
 * <p>
 * 在实际的编程过程中同步阻塞是要避免的，而I/O阻塞是需要小心优化的
 *
 * @author wangy325 tij_4th_edition
 */
public class Interrupting {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    static void test(Runnable r) throws InterruptedException {
        // 构造一个可中断的任务
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        // 中断任务
        // TODO: 为什么都是返回true?
        System.out.println(r.getClass().getSimpleName() + " Interrupt: " + f.cancel(true));
    }

    public static void main(String[] args) throws Exception {
        test(new SleepBlocked());
        test(new IOBlocked(System.in)); // 不能中断
        test(new SynchronizedBlocked()); // 不能中断
        TimeUnit.SECONDS.sleep(3);
        System.exit(0); // ... since last 2 interrupts failed
    }

    /** sleep可以被中断 */
    static class SleepBlocked implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            }
            System.out.println("Exiting SleepBlocked.run()");
        }
    }

    /** I/O不可被中断 */
    static class IOBlocked implements Runnable {
        private InputStream in;

        public IOBlocked(InputStream is) {
            in = is;
        }

        @Override
        public void run() {
            try {
                System.out.println("Waiting for read():");
                in.read();
            } catch (Exception e) {
                //
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupted from blocked I/O");
                } else {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Exiting IOBlocked.run()");
        }
    }

    /** 不可被中断 */
    static class SynchronizedBlocked implements Runnable {
        public synchronized void f() {
            while (true) // Never releases lock
                Thread.yield();
        }

        public SynchronizedBlocked() {
            // 构造之后就获取锁而不释放
            new Thread(() -> {
                f(); // Lock acquired by this thread
            }).start();
        }

        /** run()方法将一直阻塞 */
        @Override
        public void run() {
            System.out.println("Trying to call f()");
            f();
            System.out.println("Exiting SynchronizedBlocked.run()");
        }
    }
}
/*
InterruptedException
SleepBlocked Interrupt: true
Exiting SleepBlocked.run()
Waiting for read():
IOBlocked Interrupt: true
Trying to call f()
SynchronizedBlocked Interrupt: true
 *///:~
