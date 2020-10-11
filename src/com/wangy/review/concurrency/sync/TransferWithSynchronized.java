package com.wangy.review.concurrency.sync;

import java.math.BigDecimal;

/**
 * 使用synchronized关键字实现同步
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 23:15
 */
public class TransferWithSynchronized {
    static double INITIAL_MONEY = 1000;

    public static void main(String[] args) {

        int ACCOUNTS = 100;
        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);

        for (int i = 0; i < ACCOUNTS; i++) {
            Thread t = new Thread(new Transfer.TransferTask(bank));
            t.start();
        }

    }

    static class Bank extends Transfer.Bank {

        public Bank(int accountCount, double money) {
            super(accountCount, money);
        }

        @Override
        public synchronized void transfer(int from, int to, double amount) throws InterruptedException {
            if (accounts[from] < amount) wait();  // can be interrupted
            if (from == to) return;
            // transfer
            accounts[from] -= amount;
            System.out.printf("%s: transfer %5.2f from %d to %d%n",
                Thread.currentThread().getName(),
                amount,
                from,
                to);
            accounts[to] += amount;
            // 此处获取金额的方法是安全的，无需对totalBalance()方法加锁即可保证安全
            //  因为此法是被安全调用的
            System.out.printf("%s: Total Balance: %10.2f%n",
                Thread.currentThread().getName(),
                totalBalance());
            notifyAll();  // wake up all threads waiting on this monitor
        }

        @Override
        public synchronized double totalBalance() {
            double sum = 0;
            for (double a : accounts) {
                sum += a;
            }
            return sum;
        }
    }
}
