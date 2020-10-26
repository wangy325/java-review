package com.wangy.review.concurrency.component;


import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Cay Horstmann
 * @version 1.02 2015-06-21
 * @date 2020/10/26 / 16:41
 */
public class SearchKeyword {

    private static final int FILE_QUEUE_SIZE = 10;
    private static final int SEARCH_THREADS = 100;
    private static final File DUMMY = new File("");
    private static BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

    public static void main(String[] args) throws InterruptedException {
        try (Scanner in = new Scanner(System.in)) {
            String directory = "src";
            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();


            Thread r = new Thread(() -> {
                try {
                    enumerate(new File(directory));
                    queue.put(DUMMY);
                } catch (InterruptedException e) {
                    // ignore
                }
            });
            r.start();
            TimeUnit.SECONDS.sleep(1);

            // r线程肯定是阻塞的，并被take()方法唤醒
            System.out.println(r.getState());

            for (int i = 1; i <= SEARCH_THREADS; i++) {
                Runnable searcher = () -> {
                    try {
                        boolean done = false;
                        while (!done) {
                            File file = queue.take();
                            if (file == DUMMY) {
                                // 保证所有线程关闭
                                queue.put(file);
                                done = true;
                            } else {
                                search(file, keyword);
                            }
//                            Thread.yield();
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                };
                new Thread(searcher).start();
            }
        }
    }

    /**
     * Recursively enumerates all files in a given directory and its subdirectories.
     *
     * @param directory the directory in which to start
     */
    public static void enumerate(File directory) throws InterruptedException {
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
    public static void search(File file, String keyword) throws IOException {
        try (Scanner in = new Scanner(file, "UTF-8")) {
            int lineNumber = 0;
            while (in.hasNextLine()) {
                lineNumber++;
                String line = in.nextLine();
                if (line.contains(keyword)) {
                    System.out.printf("[%s] %s:%d:%s%n",Thread.currentThread().getName(), file.getPath(), lineNumber, line);
                }
            }
        }
    }
}
