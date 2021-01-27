package com.wangy.review.concurrency.sync;

/**
 * DCL(Double-Checked Locking)单例中使用了volatile关键字
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/15 / 11:18
 */
public class DoubleCheckSingleton {
    /** instance被声明为volatile */
    private static volatile DoubleCheckSingleton instance;

    private DoubleCheckSingleton() {
    }

    /**
     * the evolution of double-check lock:
     * <pre>
     *     // same as static synchronized method
     *     public static DoubleCheckSingleton getInstance() {
     *         synchronized (DoubleCheckSingleton.class) {
     *             // single check
     *             if (instance == null) {
     *                 instance = new DoubleCheckSingleton();
     *             }
     *         }
     *         return instance;
     *     }
     * </pre>
     * the double check lock is not thread safe without the instance declared with volatile
     * <pre>
     *     public static DoubleCheckSingleton getInstance() {
     *         if (instance == null) {
     *             synchronized (DoubleCheckSingleton.class) {
     *                 // the double check lock
     *                 if (instance == null) {
     *                     instance = new DoubleCheckSingleton();
     *                 }
     *             }
     *         }
     *         return instance;
     *     }
     * </pre>
     *
     * @return a multi-thread safe singleton instance
     */
    public static DoubleCheckSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckSingleton.class) {
                // the double check lock
                if (instance == null) {
                    instance = new DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        DoubleCheckSingleton instance = getInstance();
        for (; ; ) {
            new Thread(() -> {
                DoubleCheckSingleton instance1 = getInstance();
                if (!instance1.equals(instance)) {
                    System.out.println(Thread.currentThread() + ": " + instance1);
                    return;
                }
            }).start();
        }
    }
}
