package com.wangy.review.concurrency.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * <b>10/20注：在代码中使用多个对象的内部锁会降低代码的易读性</b><br>
 *
 * 比较使用synchronized保护方法和保护代码块的性能，在此例中将看到：
 * <ul>
 *     <li>使用线程安全的类包装一个线程不安全的类:
 *     <ul>
 *         <li>使用原子类{@link AtomicInteger}作为计数器</li>
 *         <li>使用{@link Collections}的同步视图保证元素同步写入</li>
 *         <li>模版方法设计模式</li>
 *     </ul>
 *     </li>
 *     <li>同步方法和同步代码块的的性能问题</li>
 * </ul>
 * 典型的输出为：
 * <pre>
 *    po1: Pair: Pair{x=127, y=127} checkCount = 19199
 *    po2: Pair: Pair{x=128, y=128} checkCount = 63835306
 * </pre>
 * 其中<i><b>po1</b></i>使用同步方法，<i><b>po2</b></i>使用同步代码块，
 * <i><b>po1</b></i>的<i>checkCount</i>不允许大于<i><b>po2</b></i>的<i>checkCount</i>,这是因为
 * <i><b>po1</b></i>对方法同步会更长时间的占用锁，此情况下<i>checkCount</i>线程的<code>getPair()</code>
 * 方法所能获取的锁的次数自然会降低
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/15 / 15:20
 */
public class CriticalSection {
    /** thread unsafe class */
    class Pair {
        private int x, y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Pair() {
            this(0, 0);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void incrementX() {
            x++;
        }

        public void incrementY() {
            y++;
        }

        @Override
        public String toString() {
            return "Pair{" +
                "x=" + x +
                ", y=" + y +
                '}';
        }

        public class PairValueNotMatchException extends RuntimeException {
            public PairValueNotMatchException() {
                super("Pair values not equal: " + Pair.this);
            }
        }

        public void checkState() {
            if (x != y) throw new PairValueNotMatchException();
        }
    }

    /** wrap Pair in a thread safe class */
    abstract class PairManager {
        AtomicInteger checkCount = new AtomicInteger(0);
        protected Pair p = new Pair();
        protected List<Pair> storage = Collections.synchronizedList(new ArrayList<>());

        public synchronized Pair getPair() {
            return new Pair(p.getX(), p.getY());
        }

        // 因使用了同步集合，store方法是同步的
        protected void store(Pair p) {
            storage.add(p);
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        public abstract void increment();
    }

    class PairManager1 extends PairManager {

        @Override
        public synchronized void increment() {
            p.incrementX();
            p.incrementY();
            store(getPair());
        }
    }

    class PairManager2 extends PairManager {

        @Override
        public void increment() {
//            Pair tmp;
            synchronized (this) {
                p.incrementX();
                p.incrementY();
            }
            // 此语句置入同步块中将会影响checkCount的值
            store(getPair());
        }
    }

    class PairOperator implements Runnable {
        private PairManager pm;

        public PairOperator(PairManager pm) {
            this.pm = pm;
        }

        @Override
        public void run() {
            while (true) pm.increment();
        }

        @Override
        public String toString() {
            return "Pair: " + pm.getPair() +
                " checkCount = " + pm.checkCount.get();
        }
    }

    class PairChecker implements Runnable {
        private PairManager pm;

        public PairChecker(PairManager pm) {
            this.pm = pm;
        }

        @Override
        public void run() {
            while (true) {
                pm.checkCount.incrementAndGet();
                pm.getPair().checkState();
            }
        }
    }

    void testApproaches() {
        PairManager pm1 = new PairManager1();
        PairManager pm2 = new PairManager2();
        ExecutorService executorService = Executors.newCachedThreadPool();
        PairOperator po1 = new PairOperator(pm1);
        PairOperator po2 = new PairOperator(pm2);

        PairChecker pc1 = new PairChecker(pm1);
        PairChecker pc2 = new PairChecker(pm2);
        executorService.execute(po1);
        executorService.execute(po2);
        executorService.execute(pc1);
        executorService.execute(pc2);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("po1: " + po1 + "\npo2: " + po2);
        System.exit(0);
    }

    public static void main(String[] args) {
        CriticalSection cs = new CriticalSection();
        cs.testApproaches();
    }

}
