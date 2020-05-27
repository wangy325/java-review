package com.wangy.review.container.collection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * simple test of PriorityQueue
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/30 / 01:07
 */
public class PriorityQueueTest {

    public static void main(String[] args) {
//        unsorted();
        userComparator();
    }

    static void unsorted() {
        Queue<Integer> pq = new PriorityQueue<>();
        pq.add(7);
        pq.add(1);
        pq.add(12);
        pq.add(6);
        pq.add(9);
        pq.add(1);
        System.out.println("pq: " + Arrays.toString(pq.toArray()));
        Object[] array = pq.toArray();
        Arrays.sort(array);
        System.out.println("sorted array: " + Arrays.toString(array));
        // the least element always in the head of queue
        pq.poll();
        pq.forEach((e) -> {
            System.out.print(e + "\t");
        });
    }

    static void userComparator() {
        class PC {
            private String model;
            private Double price;

            private PC(String model, Double price) {
                this.model = model;
                this.price = price;
            }
        }

        Queue<PC> pq = new PriorityQueue<>((o1, o2) -> (int) (o2.price - o1.price));
        pq.add(new PC("dell", 15499d));
        pq.add(new PC("apple", 18899d));
        pq.add(new PC("samsung", 8999d));
        pq.add(new PC("asus", 12999d));
        pq.add(new PC("hp", 6399d));
        pq.add(new PC("lenovo", 16999d));

        pq.forEach(e -> System.out.print(e.price + "\t"));
        System.out.println();
        pq.remove();
        pq.forEach(e -> System.out.print(e.price + "\t"));
        System.out.println();
        pq.remove();
        pq.forEach(e -> System.out.print(e.price + "\t"));
        System.out.println();

        Queue<PC> pq1 = new PriorityQueue<>((o1,o2) -> (o1.model.compareTo(o2.model)));
        pq1.add(new PC("samsung", 8999d));
        pq1.add(new PC("apple", 18899d));
        pq1.add(new PC("lenovo", 16999d));
        pq1.add(new PC("asus", 12999d));
        pq1.add(new PC("dell", 15499d));
        pq1.add(new PC("hp", 6399d));
        pq1.forEach(e -> System.out.print(e.model + "\t"));
        System.out.println();
        pq1.remove();
        pq1.forEach(e -> System.out.print(e.model + "\t"));
        System.out.println();
        pq1.remove();
        pq1.forEach(e -> System.out.print(e.model + "\t"));
    }

}
