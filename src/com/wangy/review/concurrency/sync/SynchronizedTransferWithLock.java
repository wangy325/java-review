package com.wangy.review.concurrency.sync;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 09:44
 */
public class SynchronizedTransferWithLock {
    static double INITIAL_MONEY = 1000;

    public static void main(String[] args) {

        int ACCOUNTS = 100;
        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);

        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(new TransferTask(bank));
            t.start();

            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    double v = bank.totalBalance();
                    BigDecimal bigDecimal  = new BigDecimal(v).setScale(2,BigDecimal.ROUND_HALF_UP);
                    if (bigDecimal.intValue()  != 100000){
                        System.out.println(bigDecimal +  "  is not even!");
                    }
                }
            }).start();*/
        }

    }

    static class TransferTask implements Runnable {
        private Bank bank;
        private int size;
        private double maxAmount = INITIAL_MONEY;

        public TransferTask(Bank bank) {
            this.bank = bank;
            this.size = bank.size();
        }

        @Override
        public void run() {
            try {
                int from = (int) (size * Math.random());
                int to = (int) (size * Math.random());
//                int to = (from + 1 >= size) ? 0 : from + 1;
                double amount = maxAmount * Math.random();
                bank.transfer(from, to, amount);
                Thread.sleep((long) (size * Math.random()));
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
    }

    static class Bank {
        private final double[] accounts;
        // lock
        private ReentrantLock lock;
        private Condition suficient;

        public Bank(int accountCount, double money) {
            // initialize bank account
            accounts = new double[accountCount];
            Arrays.fill(accounts, money);
            lock = new ReentrantLock();
            suficient = lock.newCondition();
        }

        public void transfer(int from, int to, double amount) throws InterruptedException {
            lock.lock();
            try {
                if (accounts[from] < amount) {
                    // could be interrupted
                    suficient.await();
                }
                ;
                if (from == to) return;
                // transfer
                accounts[from] -= amount;
                System.out.println(Thread.currentThread() + " move away");
                accounts[to] += amount;
                System.out.printf("%s: %10.2f from %d to %d, Total Balance: %10.2f%n",
                    Thread.currentThread(),
                    amount,
                    from,
                    to,
                    totalBalance());
                // invoke all waited condition
                suficient.signalAll();
            } finally {
                lock.unlock();
            }
        }

        private double totalBalance() {
            // 是否加锁应取决于资源是否共享
            lock.lock();
            try {
                double sum = 0;
                for (double a : accounts) {
                    sum += a;
                }
                // 查看线程锁计数器
                // System.out.println(lock.getHoldCount());
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
