package com.wangy.review.algorithm.queue;

import java.util.Arrays;

/**
 * 使用数组实现无界顺序队列（自动扩容）
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/28 / 17:02
 */
public class ArrayQueueUnbounded {
    private int[] data;

    // size of data array
    private int cap;
    // index of head element
    private int head;
    // index of tail element
    private int tail;

    public ArrayQueueUnbounded() {
        // initialization capacity: 4
        this.data = new int[4];
        this.cap = 4;
        this.head = this.tail = -1;
    }

    boolean isFull() {
        return tail == cap - 1;
    }

    boolean isEmpty() {
        // tail = -1
        // head = tail +1
        return tail == -1 || head == tail + 1;
    }

    // 入队
    int enQueue(int e) {
        // full, grow, and reposition
        // 若队列满了，那么增加容量，并且将队列中的元素进行重排
        // |x|x|1|2| => |1|2|-|-|-|-|
        if (isFull()) {
            cap += cap >> 1;
            int[] newData = new int[cap];
            for (int i = head; i <= tail; i++) {
                newData[i - head] = data[i];
            }
            data = newData;
            tail -= head;
            head = 0;
        }
        if (isEmpty()) {
            head++;
        }
        data[++tail] = e;
        return e;
    }

    // 出队
    int deQueue() {
        if (isEmpty()) throw new RuntimeException("队列为空！");
        return data[head++];
    }

    // 打印数组/队列
    void printAll() {
        System.out.println("数组：" + Arrays.toString(data));
        System.out.print("队列：[");
        if (!isEmpty()) {
            for (int i = head; i <= tail; i++) {
                if (i == tail)
                    System.out.print(data[i]);
                else
                    System.out.print(data[i] + ", ");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        ArrayQueueUnbounded aqu = new ArrayQueueUnbounded();
        aqu.enQueue(1);
        aqu.enQueue(2);
        aqu.enQueue(3);
        aqu.enQueue(4);
        aqu.deQueue();
        aqu.enQueue(5);
        aqu.printAll();
    }
}
