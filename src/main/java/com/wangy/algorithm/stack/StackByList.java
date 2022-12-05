package com.wangy.algorithm.stack;


/**
 * 使用链表实现一个栈，顺序栈，没有容量限制
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/24 / 16:34
 */
public class StackByList {

    private SingleNodeList data;

    public StackByList() {
        this.data = new SingleNodeList();
    }

    int count() {
        return data.size();
    }

    void push(int e) {
        data.insertHead(e);
    }

    Object pop() {
        return data.removeHead();
    }

    Object top(){
        return data.getHead();
    }

    // 按出栈顺序打印栈内元素
    void printAll(){
        data.printAll();
    }

    public static void main(String[] args) {
        StackByList stack = new StackByList();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.printAll();
        stack.pop();
        stack.printAll();
    }

    /************************* 链表实现 ***************/

    class SingleNodeList {
        private Node head;

        private int size;

        public SingleNodeList() {
            this.head = null;
            this.size = 0;
        }

        int size() {
            return size;
        }

        boolean isEmpty() {
            return size == 0;
        }

        Object getHead() {
            return head.data;
        }

        void insertHead(Object o) {
            Node n = new Node(o);
            n.next = head;
            head = n;
            size++;
        }

        Object removeHead(){
            if(head == null) throw new ArrayStoreException("Stack Empty!");
            Node h = this.head;
            this.head = this.head.next;
            size--;
            return h.data;
        }

        void printAll(){
            System.out.print("[");
            Node h = head;
            for (int i = 0; i < size -1 ; i++) {
                System.out.print(h.data + ",");
                h = h.next;
            }
            System.out.println(h.data + "]");
        }

        class Node {
            Object data;
            Node next;

            public Node(Object data) {
                this.data = data;
                this.next = null;
            }
        }
    }
}
