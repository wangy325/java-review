package com.wangy.review.concurrency.sync;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 12:31
 */
public class ClientSideLock {
    static double INITIAL_MONEY = 1000;

    public static void main(String[] args) {

        int ACCOUNTS = 100;
        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);

        for (int i = 0; i < ACCOUNTS; i++) {
            Thread t = new Thread(new TransferTask(bank));
            t.start();

            /*new Thread(() -> {
                double v = bank.totalBalance();
                BigDecimal bigDecimal  = new BigDecimal(v).setScale(2,BigDecimal.ROUND_HALF_UP);
                if (bigDecimal.intValue()  != 100000){
                    System.out.println(bigDecimal +  "  is not even!");
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
                double amount = maxAmount * Math.random();
                bank.transfer(from, to, amount);
                Thread.sleep((long) (size * Math.random()));
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
    }

    static class Bank {
        private final Vector<Double> accounts;

        public Bank(int accountCount, double money) {
            // initialize bank account
            accounts = new Vector<>(accountCount);
            List<Double> doubles = Collections.nCopies(accountCount, money);
            accounts.addAll(doubles);
        }

        public void transfer(int from, int to, double amount) {
            synchronized (accounts) {
                if (accounts.get(from) < amount) return;
                if (from == to) return;
                // transfer
                accounts.set(from, accounts.get(from) - amount);
                System.out.println(Thread.currentThread() + " move away");
                accounts.set(to, accounts.get(to) + amount);
                System.out.printf("%s: %10.2f from %d to %d, Total Balance: %10.2f%n",
                    Thread.currentThread(),
                    amount,
                    from,
                    to,
                    totalBalance());
            }
        }

        private double totalBalance() {
            double sum = 0;
            for (double a : accounts) {
                sum += a;
            }
            return sum;
        }

        int size() {
            return accounts.size();
        }
    }
}
