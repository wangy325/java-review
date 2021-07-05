package com.wangy.review.algorithm.sort;

import java.util.Arrays;

/**
 * 选择排序的Java实现<br>
 * 若有数组A[N]
 * <p>
 * 选择排序和插入排序有些类似，从第p个元素开始，每次从剩余的N-p元素中选出最小的那个元素，放在p个元素的末尾。<br>
 * 选择排序同样保证了前p个元素是已经排序的。
 * <p>
 * 若有数组【4，19，12，3，25，16】，选择排序的过程为：
 * <pre>
 *      原数组     4 19 12 3 25 16
 *      第一次     3 19 12 4 25 16
 *      第二次     3 4 12 19 25 16
 *      第三次     3 4 12 19 25 16
 *      第四次     3 4 12 16 25 19
 *      第五次     3 4 12 16 19 25
 *      第六次     3 4 12 16 19 25
 * </pre>
 * <p>
 * <p>
 * 选择排序的时间复杂度和冒泡排序一样，为O(N^2);<br>
 * 选择排序是不稳定排序，如序列【5，4，5，3，6】第一次选择后变为【3，4，5，5，6】，
 * 其中两个「5」的相对位置发生了变化。
 *
 * @author wangy
 * @version 1.0
 * @date 2021/7/5 / 14:28
 */
public class SelectSort {

    static void sort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int tmp = a[i];
            int index = i;
            // 查找最小值
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[index]) {
                    index = j;
                }
            }
            // 交换位置
            a[i] = a[index];
            a[index] = tmp;
            System.out.println("===>" + Arrays.toString(a));
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{4, 19, 12, 3, 25, 16};
        SelectSort.sort(a);
        System.out.println(Arrays.toString(a));
    }

}
