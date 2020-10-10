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
    private Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);

    public static void main(String[] args) {
        TransferWithLock twl = new TransferWithLock();
        twl.testTransfer();
    }

    void testTransfer() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new TransferTask(bank));
        }
        executorService.shutdown();
    }

    class TransferTask implements Runnable {
        private Bank bank;
        private int size;
        private double maxAmount = INITIAL_MONEY;

        public TransferTask(Bank bank) {
            this.bank = bank;
            this.size = bank.size();
        }

        @Override
        public void run() {
            // 没有访问共享资源情况下无需考虑线程安全问题
            int from = (int) (size * Math.random());
            int to = (int) (size * Math.random());
            double amount = maxAmount * Math.random();
            // 访问共享资源需要考虑线程安全
            bank.transfer(from, to, amount);
        }
    }

    class Bank {
        private final double[] accounts;
        // lock
        private final ReentrantLock lock;

        public Bank(int accountCount, double money) {
            // initialize bank account
            accounts = new double[accountCount];
            Arrays.fill(accounts, money);
            lock = new ReentrantLock();
        }

        public void transfer(int from, int to, double amount) {
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

        private double totalBalance() {
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

        int size() {
            return accounts.length;
        }
    }

}
