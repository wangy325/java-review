package com.wangy.algorithm.array;


import java.util.Arrays;

/**
 * 实现一个数组，包含基本的元素操作方法
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/21 / 16:11
 */
public class Array<E> {

    private E[] data;
    /** 数组空间大小 */
    private int capacity;
    /** 数组当前元素个数 */
    private int count;

    private static final int DEFAULT_SIZE = 10;


    public Array() {
        this(DEFAULT_SIZE);
    }

    public Array(int capacity) {
        data = (E[]) new Object[capacity];
        this.capacity = capacity;
        count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == capacity;
    }

    public int size() {
        return count;
    }

    public void grow() {
        capacity = capacity + (capacity >> 1);
        E[] newData = (E[]) new Object[capacity];
        for (int i = 0; i < count; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    /*
    添加一个元素；
    删除一个元素；
    根据index获取元素；
    判断元素是否在数组中并返回下标；
     */

    E add(E e) {
        if (count == capacity) {
            grow();
        }
        data[count++] = e;
        return e;
    }

    E add(int index, E e) {
        if (count == capacity) {
            grow();
        }
        if (index > count) {
            throw new IndexOutOfBoundsException("index must between zero and count.");
        }
        if (index == count) {
            return add(e);
        }
        for (int i = count; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = e;
        ++count;
        return e;
    }

    /**
     * 时间复杂度: O(n^2)
     *
     * @param e
     * @return
     */
    E remove(E e) {
        int index;
        if ((index = contains(e)) >= 0) {
            remove(index);
//            --count;
        }
        return e;
    }

    /**
     * 移除index索引处的元素
     * <p>
     * 时间复杂度O(n)
     *
     * @param index
     * @return
     */
    E remove(int index) {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index must between " + 0 + " and " + (count - 1) + ".");
        }
        E datum = data[index];
        for (int i = index; i < count - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--count] = null;
        return datum;
    }

    E get(int index) {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index must between 0 and " + (count - 1) + ".");
        }
        return data[index];
    }

    /**
     * 判断元素E是否存在于数组中
     * <p>
     * 时间复杂度：O(n)
     *
     * @param e
     * @return
     */
    int contains(E e) {
        for (int i = 0; i < count; i++) {
            if (data[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Array:" + Arrays.toString(data) + ", capacity:" + capacity + ", count:" + count;
    }

    public static void main(String[] args) {
        Array<Integer> array = new Array<>(2);
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);

        System.out.println("init==> " + array);
        array.remove(new Integer(1));
        System.out.println("remove==> " + array);
        array.add(0,9);
        System.out.println("add==> "+ array);
        array.add(2, 8);
        System.out.println("add==> "+ array);
        System.out.println("contains 6 ? " + (array.contains(6) != -1));
        System.out.println("contains 3 ? " + (array.contains(3) != -1));
        System.out.println("get by index 2: " + array.get(2));
    }

}
