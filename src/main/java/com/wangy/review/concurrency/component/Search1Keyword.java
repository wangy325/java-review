package com.wangy.review.concurrency.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * invokeAny 执行与取消执行
 * <p>
 * invokeAll等待所有任务执行
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/9 / 10:10
 */
public class Search1Keyword extends SearchKeyword {

    String empty = "";

    public static void main(String[] args) {
        Search1Keyword s1k = new Search1Keyword();
        s1k.find();
    }

    @Override
    void find() {
        // 带资源的try块
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter keyword (e.g. volatile): ");
            keyword = in.nextLine();

            Producer p = new Producer();
            List<Callable<String>> tasks = new ArrayList<>();

            ExecutorService pool = Executors.newCachedThreadPool();

            for (int i = 1; i <= 10; i++) {
                // run consumer
                tasks.add(new Consumer1());
            }
            pool.execute(p);
            // 此方法并不那么单纯，其结果只取一个，但是任务可能执行了多个
            String res = pool.invokeAny(tasks);
            System.out.println(res);
            /*List<Future<String>> futureList = pool.invokeAll(tasks);
            for (Future<String> future : futureList) {
                System.out.print(future.get());
            }*/
            pool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Consumer1 implements Callable<String> {

        @Override
        public String call() throws Exception {
            try {
                while (!done) {
                    File file = queue.take();
                    if (file == DUMMY) {
                        done = true;
                    } else {
                        String s = search1(file, keyword);
                        if (s.length() > 0) {
                            return s;
                        }
                    }
                }
            } catch (Exception e) {
                // ignore
            }
            return empty;
        }
    }

    public String search1(File file, String keyword) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder("");

        try (Scanner in = new Scanner(file, "UTF-8")) {
            int lineNumber = 0;
            while (in.hasNextLine()) {
                if (!Thread.interrupted()) {
                    lineNumber++;
                    String line = in.nextLine();
                    if (line.contains(keyword)) {
                        sb.append("[").append(Thread.currentThread().getName()).append("]: ")
                            .append(file.getPath()).append(lineNumber).append(line).append("\n");
                    }
                } else {
                    // thread interrupted by future.cancel()
                    System.out.printf("[%s] %s%n", Thread.currentThread().getName(), " interrupted");
                    return empty;
                }
            }
        }
        return sb.toString();
    }
}
