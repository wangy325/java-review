package com.wangy.review.algorithm.queue;

/**
 * 使用链表实现一个无界队列
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/28 / 15:09
 */
public class ListQueue {
    // index
    private Node head;

    private Node tail;

    private int size;

    public ListQueue() {
    }

    int size() {
        return size;
    }

    int enQueue(int e) {
        Node node = new Node(e);
        // empty
        if (size == 0) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        ++size;
        return e;
    }

    int deQueue() {
        if (size == 0) throw new RuntimeException("xx为空");
        int e = head.n_data;
        head = head.next;
        // head = null, 链表元素全部弹出
        if (head == null) tail = null;
        --size;
        return e;
    }

    // O(n)
    void printAll() {
        // 打印链表
        System.out.print("[");
        Node pos = head;
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                System.out.print(pos.n_data);
            } else {
                System.out.print(pos.n_data + ", ");
            }
            pos = pos.next;
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        ListQueue lq = new ListQueue();
        lq.enQueue(1);
        lq.enQueue(2);
//        lq.enQueue(3);
        lq.enQueue(4);

        lq.deQueue();
        lq.deQueue();
        lq.printAll();

    }

    /************************* 单链表 ************************/

    private class Node {
        private int n_data;
        private Node next;

        public Node(int n_data) {
            this.n_data = n_data;
            this.next = null;
        }
    }
}
