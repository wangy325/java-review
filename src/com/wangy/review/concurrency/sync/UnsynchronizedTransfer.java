package com.wangy.review.concurrency.sync;

import java.util.Arrays;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/12 / 15:15
 */
public class UnsynchronizedTransfer {
    static double INITIAL_MONEY = 1000;

    public static void main(String[] args) {

        int ACCOUNTS = 100;
        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);

        for (int i = 0; i < 2; i++) {
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

        public void transfer(int from, int to, double amount) {
            if (accounts[from] < amount) return;
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
