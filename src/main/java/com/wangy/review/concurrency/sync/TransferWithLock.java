package com.wangy.review.concurrency.sync;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用显式可重入锁保证资源安全
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 09:44
 */
@SuppressWarnings("all")
public class TransferWithLock {

    static double INITIAL_MONEY = 1000;
    static int ACCOUNTS = 100;
    private BankLock bank = new BankLock(ACCOUNTS, INITIAL_MONEY);

    public static void main(String[] args) {
        TransferWithLock twl = new TransferWithLock();
        twl.testTransfer();
    }

    void testTransfer() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Transfer.TransferTask(bank));
        }
        executorService.shutdown();
    }

    static class BankLock extends Transfer.Bank{
        // lock
        protected final ReentrantLock lock;

        public BankLock(int accountCount, double money) {
            super(accountCount,money);
            lock = new ReentrantLock();
        }

        @Override
        public void transfer(int from, int to, double amount) throws Exception {
            lock.lock();
            try {
                if (accounts[from] < amount) return;
                if (from == to) return;
                // transfer
                accounts[from] -= amount;
                System.out.printf("%s: transfer %5.2f from %d to %d%n",
                    Thread.currentThread().getName(),
                    amount,
                    from,
                    to);
                accounts[to] += amount;
                System.out.printf("%s: Total Balance: %10.2f%n",
                    Thread.currentThread().getName(),
                    totalBalance());
            } finally {
                // 保证锁一定被释放
                lock.unlock();
            }
        }

        @Override
        public double totalBalance() {
            // 是否加锁应取决于资源是否共享
            // 在此类的情况下，totalBalance方法不需要加锁
            // 因为此方法在transfer()方法中调用，而transfer()方法是序列化访问的（加锁）的
            // 所以此方法不会存在安全性问题
            // 但是由于使用的是可重入锁，因此此处可以再次获得锁，也是没有问题的
            lock.lock();
            try {
                double sum = 0;
                for (double a : accounts) {
                    sum += a;
                }
                // 查看线程锁计数器  计数器应该是2
//                System.out.println(lock.getHoldCount());
                return sum;
            } finally {
                lock.unlock();
            }
        }
    }

}
