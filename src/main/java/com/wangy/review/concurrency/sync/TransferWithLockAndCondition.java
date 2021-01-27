package com.wangy.review.concurrency.sync;

import java.math.BigDecimal;
import java.util.concurrent.locks.Condition;

/**
 * 使用显式可重入锁+条件保证资源安全
 * 使用条件要注意预防死锁的出现
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 09:44
 */
@SuppressWarnings("all")
public class TransferWithLockAndCondition {
    static double INITIAL_MONEY = 1000;
    static int ACCOUNTS = 100;

    public static void main(String[] args) {
        TransferWithLockAndCondition twac = new TransferWithLockAndCondition();
        twac.transfer();

    }

    void transfer() {
        BankLC bank = new BankLC(ACCOUNTS, INITIAL_MONEY);

        // 以下线程操作了同一个bank资源
        System.out.println(" PRESS CTRL-C TO EXIT ");
        new Thread(() -> {
            while (true) {
                // 若totalBalance方法不加锁，那么此线程一定能获得讹误的总金额数
                double v = bank.totalBalance();
                BigDecimal balance = new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (balance.intValue() != 100000) {
                    System.out.println("[" + Thread.currentThread().getName() + "]" + balance + "  is not 100000!");
                    break;
                }
            }
        }).start();

        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new Transfer.TransferTask(bank));
            t.start();
        }


    }

    class BankLC extends TransferWithLock.BankLock {
        // condition
        private final Condition suficient;

        public BankLC(int accountCount, double money) {
            super(accountCount, money);
            suficient = lock.newCondition();
        }

        @Override
        public void transfer(int from, int to, double amount) throws InterruptedException {
            lock.lock();
            try {
                // 如果账户余额不够则等余额够了再转账 可能出现死锁
                if (accounts[from] < amount) {
                    // could be interrupted
                    suficient.await();
                }
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
                /*System.out.printf("%s: Total Balance: %10.2f%n",
                    Thread.currentThread().getName(),
                    totalBalance());*/
                // 唤醒所有在此条件上等待的线程，被唤醒的线程需要重新获取锁
                suficient.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}
