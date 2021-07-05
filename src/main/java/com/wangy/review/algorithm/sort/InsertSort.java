package com.wangy.review.algorithm.sort;

import java.util.Arrays;

/**
 * 插入排序的Java实现
 * <p>
 * 假设有数组a[N]，插入排序的原理是：
 * <br>
 * 排序由N-1趟完成，从i = 1开始到N-1趟，插入排序保证从0到i-1位置上的元素是已排序的。
 * <br>
 * 因此第i趟时，只需要将位置i上的元素插入到已排序的元素中。
 * <p>
 * 假设有数组【34, 16, 25, 8, 56, 42, 17】，那么插入排序需要6趟，即可完成排序：
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 * <caption>插入排序的过程</caption>
 * <tr>
 * <td>原数组</td>
 * <td>34</td>
 * <td>16</td>
 * <td>25</td>
 * <td>8</td>
 * <td>56</td>
 * <td>42</td>
 * <td>17</td>
 * <td>移动的位置</td>
 * </tr>
 * <tr>
 * <td>第一趟</td>
 * <td>16</td>
 * <td>34</td>
 * <td>25</td>
 * <td>8</td>
 * <td>56</td>
 * <td>42</td>
 * <td>17</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>第二趟</td>
 * <td>16</td>
 * <td>25</td>
 * <td>34</td>
 * <td>8</td>
 * <td>56</td>
 * <td>42</td>
 * <td>17</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>第三趟</td>
 * <td>8</td>
 * <td>16</td>
 * <td>25</td>
 * <td>34</td>
 * <td>56</td>
 * <td>42</td>
 * <td>17</td>
 * <td>3</td>
 * </tr>
 * <td>第四趟</td>
 * <td>8</td>
 * <td>16</td>
 * <td>25</td>
 * <td>34</td>
 * <td>56</td>
 * <td>42</td>
 * <td>17</td>
 * <td>0</td>
 * </tr>
 * <td>第五趟</td>
 * <td>8</td>
 * <td>16</td>
 * <td>25</td>
 * <td>34</td>
 * <td>42</td>
 * <td>56</td>
 * <td>17</td>
 * <td>1</td>
 * </tr>
 * <td>第六趟</td>
 * <td>8</td>
 * <td>16</td>
 * <td>17</td>
 * <td>25</td>
 * <td>34</td>
 * <td>42</td>
 * <td>56</td>
 * <td>4</td>
 * </tr>
 * </table>
 *
 * <p>
 *     关于插入排序的复杂度分析，完全参考冒泡排序{@link BubbleSort}的分析过程。<br>
 *     插入排序也是稳定排序。
 * </p>
 *
 * @author wangy
 * @version 1.0
 * @date 2021/7/5 / 09:11
 */
public class InsertSort {

    // 通过比较->交换来排序
    // 平均时间复杂度O(N^2)
    static void sort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            for (int j = 0; j < i; j++) {
                // 元素交换
                if (a[j] > a[i]) {
                    int swap = a[j];
                    a[j] = a[i];
                    a[i] = swap;
                }
            }
            System.out.println("===> " + Arrays.toString(a));
        }
    }

    // 通过比较->移动来排序
    // 时间复杂度 O(N^2)
    static void sort2(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int tmp = a[i];
            int j;
            for (j = i; j > 0 && a[j - 1] > tmp; j--) {
                // 元素右移
                a[j] = a[j - 1];
            }
            a[j] = tmp;
            System.out.println("===> " + Arrays.toString(a));
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{34, 16, 25, 8, 56, 42, 17};

        InsertSort.sort(a);

        System.out.println(Arrays.toString(a));
    }
}
