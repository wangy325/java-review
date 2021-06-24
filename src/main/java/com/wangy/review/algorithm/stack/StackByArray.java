package com.wangy.review.algorithm.stack;

/**
 * 使用数组实现一个栈，并实现自动扩容
 * <p>
 * 使用数组实现的栈叫做 <b>【顺序栈】</b>
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/24 / 13:54
 */
public class StackByArray {

    private Object[] data;

    private final int DEFAULT_SIZE = 10;

    private int size;

    private int cap;

    public StackByArray() {
        this.data = new Object[DEFAULT_SIZE];
        this.cap = DEFAULT_SIZE;
    }

    public StackByArray(int cap) {
        this.data = new Object[cap];
        this.cap = cap;
    }

    boolean isEmpty() {
        return size == 0;
    }

    boolean isFull() {
        return size == cap;
    }

    int size() {
        return size;
    }

    // 按照出栈顺序打印栈元素
    void printAll() {
        System.out.print("[");
        for (int i = size; i > 1; ) {
            System.out.print(data[--i] + ", ");
        }
        System.out.println(isEmpty() ? "]" : data[0] + "]");
    }

    /**
     * 入栈, 将数据添加到右边
     * <p>
     * 当栈空间满时，将栈容量增加1倍
     * <p>
     * 均摊时间复杂度：O(1)
     *
     * @param e 待入栈元素
     */
    void push(int e) {
        if (isFull()) {
            cap =  cap + cap;
            Object[] newData = new Object[cap];
            // copy data
            for (int i = 0; i < size ; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
        data[size++] = e;
    }

    /**
     * 出栈，右边先出
     */
    Object pop() {
        if (isEmpty()) throw new ArrayStoreException("Stack Empty!");

        return data[--size];
    }

    public static void main(String[] args) {
        StackByArray stack = new StackByArray(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.printAll();
        stack.pop();
        stack.pop();
        stack.printAll();

    }
}
