package com.wangy.review.concurrency.component;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 阻塞队列使用示例1，查找关键字
 *
 * @author Cay Horstmann
 * @version 1.02 2015-06-21
 * @date 2020/10/26 / 16:41
 */
public class SearchKeyword {

    private static final int FILE_QUEUE_SIZE = 10;
    private static final int SEARCH_THREADS = 100;
    private static final File DUMMY = new File("");
    private final BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);
    private final static String DIR = "src";
    private String keyword;
    private volatile boolean done = false;

    public static void main(String[] args) {
        SearchKeyword sk = new SearchKeyword();
        sk.find();
    }

    void find() {
        // 带资源的try块
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter keyword (e.g. volatile): ");
            keyword = in.nextLine();

            Producer p = new Producer();
            Consumer c = new Consumer();

            ExecutorService pool = Executors.newCachedThreadPool();

            pool.execute(p);

            for (int i = 1; i <= SEARCH_THREADS; i++) {
                // run consumer
                pool.execute(c);
            }
            pool.shutdown();
        }
    }

    class Producer implements Runnable {

        @Override
        public void run() {
            try {
                enumerate(new File(DIR));
                queue.put(DUMMY);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                while (!done) {
                    File file = queue.take();
                    if (file == DUMMY) {
                        done = true;
                    } else {
                        search(file, keyword);
                    }
//                  Thread.yield();
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * Recursively enumerates all files in a given directory and its subdirectories.
     *
     * @param directory the directory in which to start
     */
    public void enumerate(File directory) throws InterruptedException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                enumerate(file);
            } else {
                queue.put(file);
            }
        }
    }

    /**
     * Searches a file for a given keyword and prints all matching lines.
     *
     * @param file    the file to search
     * @param keyword the keyword to search for
     */
    public void search(File file, String keyword) throws IOException {
        try (Scanner in = new Scanner(file, "UTF-8")) {
            int lineNumber = 0;
            while (in.hasNextLine()) {
                lineNumber++;
                String line = in.nextLine();
                if (line.contains(keyword)) {
                    System.out.printf("[%s] %s:%d:%s%n", Thread.currentThread().getName(), file.getPath(), lineNumber, line);
                }
            }
        }
    }
}
