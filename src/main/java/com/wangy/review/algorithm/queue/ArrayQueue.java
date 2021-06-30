package com.wangy.review.algorithm.queue;

import java.util.Arrays;

/**
 * 使用数组实现一个固定容量的顺序队列。
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/28 / 15:09
 */
public class ArrayQueue {

    private final int[] data;
    // cap of data array
    private final int cap;
    // index of head element
    private int head;
    // index of tail element
    private int tail;

    public ArrayQueue(int cap) {
        this.data = new int[cap];
        this.cap = cap;
        head = tail = -1;
    }

    // 入队
    int enQueue(int e) {
        // full
        if (isFull()) throw new ArrayStoreException("队列满了");
        // empty
        if (isEmpty()) {
            head++;
        }
        data[++tail] = e;
        return e;
    }

    // 出队
    int deQueue() {
        // empty
        if (isEmpty()) throw new ArrayStoreException("队列为空");
        return data[head++];
    }

    int getHead() {
        if (isEmpty()) return -1;
        return data[head];
    }

    int getTail() {
        if (isEmpty()) return -1;
        return data[tail];
    }

    boolean isFull() {
        return tail == cap - 1;
    }

    boolean isEmpty() {
        // tail = -1
        // head = cap
        return tail == -1 || head == cap;
    }

    // 打印数组/队列
    void printAll() {
        System.out.println("数组：" + Arrays.toString(data));
        System.out.print("队列：[");
        if (!isEmpty()) {
            //
            for (int i = head; i <=tail; i++) {
                if (i == tail)
                    System.out.print(data[i]);
                else
                    System.out.print(data[i] + ", ");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        ArrayQueue aq = new ArrayQueue(3);
        aq.enQueue(1);
        aq.enQueue(2);
        aq.enQueue(3);
        System.out.println(aq.deQueue());
        System.out.println(aq.deQueue());
//        System.out.println(aq.deQueue());
//        System.out.println(aq.deQueue());

        System.out.printf("队列头尾索引和值：%s: %s, %s: %s%n", aq.head, aq.getHead(), aq.tail, aq.getTail());
        aq.printAll();
    }
}
