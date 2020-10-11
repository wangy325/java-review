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
    static int ACCOUNTS = 100;

    public static void main(String[] args) {

        Transfer t = new Transfer();
        t.test();
    }

    void test() {
        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);
        // 增加线程数可以提高出现讹误的概率
        for (int i = 0; i < 2; i++) {
            // 创建线程时使用共享资源bank
            Thread t = new Thread(new TransferTask(bank));
            t.start();
        }
    }

    /** 使用静态内部类是为了后续获取实例更方便（不需要外围类实例） */
    static class TransferTask implements Runnable {
        private final Bank bank;
        private final int size;
        private final double maxAmount = INITIAL_MONEY;

        public TransferTask(Bank bank) {
            this.bank = bank;
            this.size = bank.size();
        }

        @Override
        public void run() {
            //没有访问共享资源情况下无需考虑线程安全问题
            int from = (int) (size * Math.random());
            int to = (int) (size * Math.random());
            // 如果修改转账金额，提防出现死锁
//            double amount = maxAmount * (Math.random() + Math.random());
            double amount = maxAmount * Math.random();
            try {
                bank.transfer(from, to, amount);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    static class Bank {
        protected final double[] accounts;

        public Bank(int accountCount, double money) {
            // initialize bank account
            accounts = new double[accountCount];
            Arrays.fill(accounts, money);
        }

        protected void transfer(int from, int to, double amount) throws Exception {
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

        protected double totalBalance() {
            double sum = 0;
            for (double a : accounts) {
                sum += a;
            }
            return sum;
        }

        public int size() {
            return accounts.length;
        }
    }
}
