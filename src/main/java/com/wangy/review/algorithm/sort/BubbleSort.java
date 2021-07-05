package com.wangy.review.algorithm.sort;

import java.util.Arrays;

/**
 * 冒泡排序的Java实现
 * <p>
 * 时间复杂度O(n^2)，空间复杂度O(1)，稳定排序
 * </p>
 * <b>主要理解思路，实际开发应用基本用不到。</b>
 * <p>
 * 对于互异数序列，<code>4 5 6 3 2 1</code>，使用冒泡排序对其进行排序应该如何操作？
 * <p>
 * 冒泡排序的思路：每次冒泡依次比较序列中相邻的元素大小，若前者>后者，则将二者顺序调换。
 * 经过一次冒泡，可以保证<b>一个元素在正确的位置上：</b>
 * <pre>
 *     第一次冒泡的过程：
 *       -> 4 5 6 3 2 1
 *       -> 4 5 6 3 2 1
 *       -> 4 5 3 6 2 1
 *       -> 4 5 3 2 6 1
 *       -> 4 5 3 2 1 6
 *     经过一次冒泡之后，「6」在其正确的位置上了。
 * </pre>
 * 下面是整个冒泡排序的过程：
 * <pre>
 *     第一次：4 5 3 2 1 6
 *     第二次：4 3 2 1 5 6
 *     第三次：3 2 1 4 5 6
 *     第四次：2 1 3 4 5 6
 *     第五次：1 2 3 4 5 6
 *     第六次：1 2 3 4 5 6
 * </pre>
 * 时间复杂度分析：
 * <p>
 * 先抛结论：对于N个互异数，通过交换相邻元素进行排序的任何算法（冒泡，插入，选择）时间复杂度都为O(N^2)。
 * <p>
 * 对于互异数组（不考虑数组内有相同元素的情况）a[N]，若<code>a[i] > a[j](i<j)</code>，称（a[i],a[j]）为a[N]的一个逆序对；
 * 反之，若<code>a[i] > a[j](i>j)</code>，称（a[i],a[j]）为a[N]的一个有序对。
 * <br>
 * 事实上，对互异数排序就是将逆序对通过位置交换变为有序对的过程，因此，有多少逆序对，就需要进行多少次元素交换。
 * <br>
 * 对于a[N]，最好的情形下，它是完全排序的，那么他的逆序对是0，时间复杂度是O(1);
 * <br>
 * 最坏的情形，它是完全反序的，那么它的逆序对就是N(N-1)/2 (Cn2)个，时间复杂度是O(N^2);
 * <br>
 * 简化分析，直接取N(N-1)/4为平均情形的逆序对个数，因此平均复杂度为O(N^2)。
 *
 * @author wangy
 * @version 1.0
 * @date 2021/7/2 / 16:07
 */
public class BubbleSort {
    /**
     * 复杂度分析：
     * 无论在什么情况下，大小为N的数组，都要经过N（N-1）/2 次比较过程，
     * 因此此算法的时间复杂度为O(N^2)
     * <br>
     * 不过，由于数组的容量是固定的，并且比较过程中只使用了2个变量，
     * 故此方法的空间复杂度为O(1)
     * <br>
     * 实际上，这个方法还可以优化，如果已经排序好了，那么就不需要再去比较了。
     * 这样可以大大降低复杂度。
     *
     * @param a 待排序数组
     */
    static void bubble(int[] a) {
        int size = a.length;
        for (int i = 0; i < size; i++) {
            // 内层循环边界为（size-i-1）
            // 因第i次冒泡，应有i个元素已经处于正确的位置，无需再比较
            for (int j = 0; j < size - i - 1; j++) {
                // swap if f > e
                int f = a[j];
                int e = a[j + 1];
                if (f > e) {
                    a[j] = e;
                    a[j + 1] = f;
                }
                System.out.println("\t====>\t" + Arrays.toString(a));
            }
            System.out.println(Arrays.toString(a));
        }
    }

    // 内层循环如果没有数据交换 --> 已经完全排序，可以跳出冒泡过程。
    static void bubble2(int[] a) {
        int size = a.length;
        for (int i = 0; i < size; i++) {
            boolean swaped = false;
            // 内层循环边界为（size-i-1）
            // 因第i次冒泡，应有i个元素已经处于正确的位置，无需再比较
            for (int j = 0; j < size - i - 1; j++) {
                // swap if before > after
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    swaped = true;
                }
                System.out.println("\t====>\t" + Arrays.toString(a));
                if (!swaped) break;
            }
            System.out.println(Arrays.toString(a));
        }
    }

    public static void main(String[] args) {
//        int[] a = new int[]{4, 5, 6, 3, 2, 1};
        int[] a = new int[]{1, 2, 3, 4, 5, 6};
        BubbleSort.bubble2(a);
    }
}
