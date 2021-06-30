package com.wangy.review.algorithm.queue;

/**
 * 使用链表实现一个无界队列
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/28 / 15:09
 */
public class ListQueue {

    private SingleNodeList data;
    // index
    private SingleNodeList.Node head;

    private int tail;

    public ListQueue() {
        this.data = new SingleNodeList();
    }

    int size() {
        return data.size;
    }

    int enQueue(int e) {
        data.addTail(e);
        return e;
    }

    int deQueue() {
        return data.removeHead();
    }

    // O(n)
    void printAll() {
        // 打印链表
        data.printAll();
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


    /************************* 单链表实现 ************************/

    private class SingleNodeList {

        private Node n_head;
        private Node n_tail;

        private int size;

        public SingleNodeList() {
        }

        int addTail(int e) {
            Node node = new Node(e);
            // empty
            if (size == 0) {
                n_head = n_tail = node;
            } else {
                n_tail.next = node;
                n_tail = node;
            }
            ++size;
            return e;
        }

        int removeHead() {
            if (size == 0) {
                throw new RuntimeException("xx为空");
            }
            int e = n_head.n_data;
            n_head = n_head.next;
            --size;
            return e;
        }

        void printAll() {
            System.out.print("[");
            Node pos = n_head;
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

        private class Node {
            private int n_data;
            private Node next;

            public Node(int n_data) {
                this.n_data = n_data;
                this.next = null;
            }
        }
    }

}
