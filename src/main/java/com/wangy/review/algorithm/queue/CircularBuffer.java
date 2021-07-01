package com.wangy.review.algorithm.queue;

import java.util.Arrays;

/**
 * 有界循环队列，实际上是循环数组实现。
 * 参考：<a href="http://www.mathcs.emory.edu/~cheung/Courses/171/Syllabus/8-List/array-queue2.html">
 * Implementing a Queue using a circular array</a>
 * <p>
 * 另一种实现思路：<a href="https://www.baeldung.com/java-ring-buffer">Implementing a Ring Buffer in Java</a>
 * <p>
 * 上述两种思路的主要差别是：
 * <ul>
 *     <li>
 *         read(入队)指针的指向不同。前者指向的是空槽，而后者指向的是队尾的元素；
 *     </li>
 *     <li>
 *         指针的内涵不同，前者对应的是数组的index，而后者对应的是入队/出队的次数；
 *     </li>
 * </ul>
 * <p>
 * 这种设计的差别，最终导致队列满/空判断上面的差异。
 * <p>
 *     <pre>
 *     循环队列的数据简单模型图——有8个slot的定容数组：此时count = 4
 *     (o代表此槽为空，N为数组容量）
 *         =========================================================
 *            [0]    [1]    [2]    [3]    [4]   [5]    [6]    [7]
 *         ｜  o  ｜  o  ｜  12 ｜  34  ｜ 65  ｜ 26  ｜  o  ｜  o  ｜
 *                         read                       write
 *         =========================================================
 *     </pre>
 * <p>
 * 在之前的顺序队列中，如果<code>write = N && read!=0</code>的情况下，数组的空间可以重复利用，
 * 当时的处理办法是对数组中的元素进行"整理"；
 * <p>
 * 在循环队列中，我们无需再这样做，队列中的slot可以重复使用，slot的值可以一直被覆盖写入，而队列真正
 * 需要关心的内容是<b>read</b>和<b>write</b>指针：<br>
 * <pre>
 *     当循环队列入队时，
 *         =========================================================
 *            [0]    [1]    [2]    [3]    [4]   [5]    [6]    [7]
 *         ｜  o  ｜  o  ｜  12 ｜  34  ｜ 65  ｜ 26  ｜  19  ｜  o  ｜
 *                         read                               write
 *         =========================================================
 *         =========================================================
 *            [0]    [1]    [2]    [3]    [4]   [5]    [6]    [7]
 *         ｜  o  ｜  o  ｜  12 ｜  34  ｜ 65  ｜ 26  ｜  19  ｜  32  ｜
 *           write         read
 *         =========================================================*
 *      write指针后移一位，即<code>write +=write</code>。
 *      不过，当write指针指向数组的末尾时，write该如何计算呢？
 *      事实上，由于循环数组的slot一直在重用，故write的取值总介于[0，N)之间，因此当有元素入队时，可用
 *      <code>write = (write +1) % N</code>计算得到write指针指向。
 *
 *      同理，当循环队列有元素出队时，
 *         =========================================================
 *            [0]    [1]    [2]    [3]    [4]   [5]    [6]    [7]
 *         ｜  o  ｜  o  ｜  12 ｜  34  ｜ 65  ｜ 26  ｜  19  ｜  o  ｜
 *                                read                        write
 *         =========================================================
 *       和入队类似，<code>read = (read + 1) % N</code>
 * </pre>
 * </p>
 * 不难发现，如果read指针追上write指针，即read==write时，队列为空；反之，若write指针追上read指针，即read=write时，
 * 队列为满。<b>如何消除歧义？</b>
 * <p>
 * 有2种办法消除歧义：
 * <ul>
 *     <li>1. 引入整型变量{@link CircularBuffer#count}，用于实时统计队列中的元素个数</li>
 *     <li>
 *         2. 取个巧，将write指针对应的slot闲置，如果发现write指针的下一个指针是read指针的时候，即认为队列满了。
 *         这种情况下，队列实际的容量为N-1。
 *     </li>
 * </ul>
 * <p>
 * 如果按照方法2，队列为空的判断条件不变，仍为<code>read == write</code>；但是队列为满的条件变了，即若
 * <code>
 *     read = (write + 1) % N
 * </code>
 * 时，认为队列已满。
 * <p>
 * 循环数组的效率很高，空间复杂度为O(1)，入队和出队的时间复杂度均为O(1)。
 *
 * @author wangy
 * @version 1.0
 * @date 2021/7/1 / 10:52
 * @see ArrayQueue
 * @see CircularBufferVariety
 */
public class CircularBuffer {
    private int[] data;

    private int read;

    private int write;
    // 队列中元素计数
    private int count;

    public CircularBuffer(int cap) {
        this.data = new int[cap];
        this.read = this.write = this.count = 0;
    }

    boolean isFull() {
        return (write + 1) % data.length == read;
    }

    boolean isEmpty() {
        return read == write;
    }

    int enQueue(int e) {
        if (isFull()) throw new RuntimeException("Queue full!");
        data[write] = e;
        write = (write + 1) % data.length;
        return e;
    }


    int deQueue() {
        if (isEmpty()) throw new RuntimeException("Queue empty!");
        int e = data[read];
        read = (read + 1) % data.length;
        return e;
    }

    public static void main(String[] args) {
        // 实际上，仅可以入队3个元素
        CircularBuffer cb = new CircularBuffer(4);
        cb.enQueue(1);
        cb.deQueue();
        cb.enQueue(2);
        cb.enQueue(3);
        cb.enQueue(5);
        cb.deQueue();
        cb.enQueue(7);
        System.out.println(Arrays.toString(cb.data));
        System.out.printf("read: %s; write: %s%n", cb.read, cb.write);
        // full exception
        cb.enQueue(9);
    }
}
