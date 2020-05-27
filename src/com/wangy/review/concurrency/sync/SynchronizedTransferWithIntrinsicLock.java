package com.wangy.review.concurrency.sync;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 使用synchronized关键字实现同步
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 23:15
 */
public class SynchronizedTransferWithIntrinsicLock {
    static double INITIAL_MONEY = 1000;

    public static void main(String[] args) {

        int ACCOUNTS = 100;
        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);

        for (int i = 0; i < ACCOUNTS; i++) {
            Thread t = new Thread(new TransferTask(bank));
            t.start();

            new Thread(() -> {
                double v = bank.totalBalance();
                BigDecimal bigDecimal  = new BigDecimal(v).setScale(2,BigDecimal.ROUND_HALF_UP);
                if (bigDecimal.intValue()  != 100000){
                    System.out.println(bigDecimal +  "  is not even!");
                }
            }).start();
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

        public Bank(int accountCount, double money) {
            // initialize bank account
            accounts = new double[accountCount];
            Arrays.fill(accounts, money);
        }

        public synchronized void transfer(int from, int to, double amount) throws InterruptedException {
            if (accounts[from] < amount) wait();  // can be interrupted
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
            notifyAll();  // wake up all threads waiting on this monitor
        }

        private synchronized double totalBalance() {
            double sum = 0;
            for (double a : accounts) {
                sum += a;
            }
            return sum;
        }

        int size() {
            return accounts.length;
        }
    }
}
