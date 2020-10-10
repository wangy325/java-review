package com.wangy.review.concurrency.sync;

import java.util.Arrays;

/**
 * 经典的非同步转账问题
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 15:15
 */
public class Transfer {
    static double INITIAL_MONEY = 1000;

    public static void main(String[] args) {

        int ACCOUNTS = 100;
        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);
        // 增加线程数可以提高出现讹误的概率
        for (int i = 0; i < 2; i++) {
            // 创建线程时使用共享资源bank
            Thread t = new Thread(new TransferTask(bank));
            t.start();
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
            int from = (int) (size * Math.random());
            int to = (int) (size * Math.random());
            double amount = maxAmount * Math.random();
            // 在线程内对共享资源进行修改需要注意安全性
            bank.transfer(from, to, amount);
            /*try {
                Thread.sleep((long) (size * Math.random()));
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }*/
        }
    }

    static class Bank {
        private final double[] accounts;

        public Bank(int accountCount, double money) {
            // initialize bank account
            accounts = new double[accountCount];
            Arrays.fill(accounts, money);
        }

        public void transfer(int from, int to, double amount) {
            if (accounts[from] < amount) return;
            if (from == to) return;
            // transfer
            accounts[from] -= amount;
            // 这个输出语句提高了线程让出cpu时间的概率
            System.out.printf("%s: transfer %10.2f from %d to %d%n",
                Thread.currentThread(),
                amount,
                from,
                to);
            accounts[to] += amount;
            System.out.printf("%s: Total Balance: %10.2f%n",
                Thread.currentThread(),
                totalBalance());
        }

        private double totalBalance() {
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
