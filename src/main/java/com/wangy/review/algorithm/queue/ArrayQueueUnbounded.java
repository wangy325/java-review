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
    /*
       关于tail指针，一般是将tail指向最新的可以插入元素的index，即tail是一个空槽（slot）：
       ===================================================
        data[0]                                   data[7]
        |    |    |  5  |  6  |  9  |  16  |      |     |
                   head                      tail
       ===================================================
        初始化时，head = tail = 0，此后每插入一个元素，tail后移一位。

        当head = tail时，认为队列已空，因为tail是一个空槽：
        ===================================================
        data[0]                                   data[7]
        |    |    |     |     |     |     |      |     |
                                            tail
                                            head
        ===================================================

        当tail = cap时，即认为队列"已满"：
        ========================================================
        data[0]                                   data[7]
        |    |    |  5  |  6  |  9  |  16  |  87  |  56  |
                   head                                    tail
        ========================================================

        有一个特殊的情况，即 head = tail = cap-1 时，此时队列是空的，也是满的，
        不过其不影响队列后续的入队：
        ===================================================
        data[0]                                   data[7]
        |    |    |     |     |     |     |      |     |
                                                   tail
                                                   head
        ===================================================
     */
    // index of tail element + 1
    private int tail;

    public ArrayQueueUnbounded() {
        // initialization capacity: 4
        this.data = new int[4];
        this.cap = 4;
        this.head = this.tail = 0;
    }

    boolean isFull() {
        // 当tail指向最后一个空槽时，即认为队列已满
        return tail == cap ;
    }

    boolean isEmpty() {
        // 无论指针在何处，只要2个指针"相遇"，即认为队列为空
        return head == tail;
    }

    // 入队 平均时间复杂度O(1)
    int enQueue(int e) {
        // 若队列满了，那么增加容量，并且将队列中的元素进行重排
        // |x|x|1|2| => |1|2|-|-|-|-|
        if (isFull()) {
            cap += cap >> 1;
            int[] newData = new int[cap];
            for (int i = head; i < tail; i++) {
                newData[i - head] = data[i];
            }
            data = newData;
            tail -= head;
            head = 0;
        }
        data[tail++] = e;
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
        ArrayQueueUnbounded aqu = new ArrayQueueUnbounded();
        aqu.enQueue(1);
        aqu.deQueue();
        aqu.enQueue(2);
        aqu.enQueue(3);
        aqu.enQueue(4);
        aqu.deQueue();
        aqu.deQueue();
        aqu.deQueue();
//        aqu.deQueue();
        aqu.enQueue(5);
        aqu.printAll();
    }
}
