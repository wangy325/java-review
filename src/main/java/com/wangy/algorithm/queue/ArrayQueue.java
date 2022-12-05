package com.wangy.algorithm.queue;

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
    /*
        关于tail指针，一般是将tail指向最新的可以插入元素的index，即tail是一个空槽（slot）：
        ===================================================
         data[0]                                   data[7]
         |    |    |  5  |  6  |  9  |  16  |      |     |
                    head                      tail
        ===================================================
         初始化时，head = tail = 0，此后每插入一个元素，tail后移一位。

         当tail = cap且head=0，即认为队列"已满"：
         ===================================================
         data[0]                                   data[7]
         |  14  |  8  |  5  |  6  |  9  |  16  |  87  |  67  |
           head                                                 tail
         ===================================================

         当head = tail时，元素都已出队，此时队列为空：
         ===================================================
         data[0]                                   data[7]
         |    |    |     |     |     |      |      |     |
                                       head
                                       tail
         ===================================================

         特殊地，当tail = cap且head != 0时，数组明明有空间，元素却无法入队，这时候就需要将head-tail指针重新整理了：
         =======================================================
         data[0]                                   data[7]
         |    |    |     |     |     |  18  |  34  |  45  |
                                       head                 tail
         ======================================================================
           |  |            data[0]                                     data[7]
            \  \---\¯\     | 18  |  34  |  45  |     |     |     |      |     |
             \  \--/_/      head                 tail
                         ======================================================
      */
    // index of tail
    private int tail;

    public ArrayQueue(int cap) {
        this.data = new int[cap];
        this.cap = cap;
        head = tail = 0;
    }


    int getHead() {
        if (isEmpty()) return -1;
        return data[head];
    }

    int getTail() {
        if (isEmpty()) return -1;
        return data[tail - 1];
    }

    boolean isFull() {
        return tail == cap && head == 0;
    }

    boolean isEmpty() {
        return head == tail;
    }

    boolean reposition() {
        return tail == cap && head != 0;
    }

    // 入队
    int enQueue(int e) {
        // full
        if (isFull()) throw new ArrayStoreException("队列满了");
        // 整理元素
        if (reposition()) {
            for (int i = head; i < tail; i++) {
                data[i - head] = data[i];
                data[i] = 0;
            }
            tail -= head;
            head = 0;
        }
        data[tail++] = e;
        return e;
    }

    // 出队
    int deQueue() {
        // empty
        if (isEmpty()) throw new ArrayStoreException("队列为空");
        return data[head++];
    }


    // 打印数组/队列
    void printAll() {
        System.out.println("数组：" + Arrays.toString(data));
        System.out.print("队列：[");
        if (!isEmpty()) {
            //
            for (int i = head; i < tail; i++) {
                if (i == tail - 1)
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
        System.out.println(aq.deQueue());
        aq.enQueue(2);
        aq.enQueue(3);
        aq.enQueue(4);

        System.out.println(aq.deQueue());
        System.out.println(aq.deQueue());
//        System.out.println(aq.deQueue());

        System.out.printf("队列头尾索引和值：%s: %s, %s: %s%n", aq.head, aq.getHead(), aq.tail, aq.getTail());
        aq.printAll();
    }
}
