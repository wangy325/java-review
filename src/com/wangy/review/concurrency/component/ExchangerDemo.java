package com.wangy.review.concurrency.component;

import com.wangy.helper.util.BasicGenerator;
import com.wangy.helper.util.Fat;
import com.wangy.helper.util.Generator;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/18 / 23:22
 */
public class ExchangerDemo {

    private class ExchangerProducer<T> implements Runnable {
        private Generator<T> generator;
        private Exchanger<List<T>> exchanger;
        private List<T> holder;

        ExchangerProducer(Exchanger<List<T>> ex, Generator<T> gen, List<T> holder) {
            exchanger = ex;
            generator = gen;
            this.holder = holder;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    for (int i = 0; i < size; i++) {
                        holder.add(generator.next());
                    }
                    // Exchange full for empty:
                    holder = exchanger.exchange(holder);
                }
            } catch (InterruptedException e) {
                // OK to terminate this way.
            }
        }
    }

    class ExchangerConsumer<T> implements Runnable {
        private Exchanger<List<T>> exchanger;
        private List<T> holder;
        private volatile T value;

        ExchangerConsumer(Exchanger<List<T>> ex, List<T> holder) {
            exchanger = ex;
            this.holder = holder;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    holder = exchanger.exchange(holder);
                    for (T x : holder) {
                        value = x; // Fetch out value
                        // CopyOnWriteArrayList 不会抛出ConcurrentModificationException
                        holder.remove(x);
                    }
                }
            } catch (InterruptedException e) {
                // OK to terminate this way.
            }
            System.out.println("Final value: " + value);
        }
    }

    static int size = 10;
    static int delay = 1; // Seconds

    @SneakyThrows
    void test() {
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<List<Fat>> xc = new Exchanger<>();
        List<Fat>
            producerList = new CopyOnWriteArrayList<>(),
            consumerList = new CopyOnWriteArrayList<>();
        exec.execute(this.new ExchangerProducer<>(xc, BasicGenerator.create(Fat.class), producerList));
        exec.execute(this.new ExchangerConsumer<>(xc, consumerList));
        TimeUnit.SECONDS.sleep(delay);
        exec.shutdownNow();
    }

    public static void main(String[] args) {
        ExchangerDemo ed = new ExchangerDemo();
        ed.test();
    }
}
