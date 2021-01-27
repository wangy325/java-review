package com.wangy.review.concurrency.sync;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * 在对象上同步，这种方法一般不建议使用
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 12:31
 */
public class TransferWithClientSideLock {
    static double INITIAL_MONEY = 1000;
    static int ACCOUNTS = 100;

    public static void main(String[] args) {

        Bank bank = new Bank(ACCOUNTS, INITIAL_MONEY);

        for (int i = 0; i < ACCOUNTS; i++) {
            Thread t = new Thread(new Transfer.TransferTask(bank));
            t.start();
        }

    }


    static class Bank extends Transfer.Bank {
        private final Vector<Double> vaccount;

        public Bank(int accountCount, double money) {
            super(accountCount, money);
            vaccount = new Vector<>(accountCount);
            List<Double> doubles = Collections.nCopies(accountCount, money);
            vaccount.addAll(doubles);
        }

        @Override
        public void transfer(int from, int to, double amount) {
            // 尽管vector是安全的，此处仍然需要使用同步代码块，因为set/get方法同步不能保证transfer()的同步
            synchronized (vaccount) {
                if (vaccount.get(from) < amount) return;
                if (from == to) return;
                // transfer
                vaccount.set(from, vaccount.get(from) - amount);
                System.out.printf("%s: transfer %6.2f from %d to %d%n",
                    Thread.currentThread(),
                    amount,
                    from,
                    to);
                vaccount.set(to, vaccount.get(to) + amount);
                System.out.printf("%s: Total Balance: %10.2f%n",
                    Thread.currentThread(),
                    totalBalance());
            }
        }

        @Override
        public double totalBalance() {
            double sum = 0;
            for (double a : vaccount) {
                sum += a;
            }
            return sum;
        }

        @Override
        public int size() {
            return vaccount.size();
        }
    }
}
