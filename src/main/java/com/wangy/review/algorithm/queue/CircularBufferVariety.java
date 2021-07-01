package com.wangy.review.algorithm.queue;

import java.util.Arrays;

/**
 * 队列的循环数组另一种实现，参考：
 * <a href="https://www.baeldung.com/java-ring-buffer">Implementing a Ring Buffer in Java</a>
 *
 * <p>
 * 这种方法的解决思路是：
 * <ul>
 *     <li>
 *         1. 引入2个变量，{@link CircularBufferVariety#readSequence}和{@link CircularBufferVariety#writeSequence},
 *         分别记录元素入队和出队的次数；初始化时，readSequence = 0， writeSequence = -1；writeSequence=-1的好处在于其实际上
 *         相当于<b>直接指向</b>了队列队尾元素的index；
 *     </li>
 *     <li>
 *         2. 为了循环使用数组中的slot，可以使用<code>writeSequence % N</code>取模的方式找到当前队列队尾的元素对应的index；
 *     </li>
 *     <li>
 *         3. 当readSequence > writeSequence时，即可认为队列为空；
 *     </li>
 *     <li>
 *         4. 当（writeSequence - readSequence）+ 1 > {@link CircularBufferVariety#data}.length时，即可认为队列已满；
 *     </li>
 * </ul>
 *
 * @author wangy
 * @version 1.0
 * @date 2021/7/1 / 14:53
 * @see CircularBuffer
 */
public class CircularBufferVariety {
    private int[] data;
    // 出队次数
    private int readSequence;
    // 入队次数
    private int writeSequence;
    // 队列中元素的个数，等于 writeSequence - readSequence + 1
    private int count;

    public CircularBufferVariety(int cap) {
        this.data = new int[cap];
        this.readSequence = 0;
        this.writeSequence = -1;
        this.count = 0;
    }

    boolean isEmpty() {
        return readSequence > writeSequence;
    }

    boolean isFull() {
        return writeSequence - readSequence + 1 == data.length;
    }

    int enQueue(int e) {
        if (isFull()) throw new RuntimeException("Queue full!");
        data[++writeSequence % data.length] = e;
        return e;
    }

    int deQueue() {
        if (isEmpty()) throw new RuntimeException("Queue empty!");
        return data[readSequence++ % data.length];
    }

    public static void main(String[] args) {
        CircularBufferVariety cb = new CircularBufferVariety(4);
        cb.enQueue(1);
        cb.deQueue();
        cb.enQueue(2);
        cb.enQueue(3);
        cb.enQueue(5);
        cb.deQueue();
        cb.enQueue(7);
        System.out.println(Arrays.toString(cb.data));
        cb.enQueue(9);
        System.out.println(Arrays.toString(cb.data));
        // full exception
        cb.enQueue(11);
    }
}
