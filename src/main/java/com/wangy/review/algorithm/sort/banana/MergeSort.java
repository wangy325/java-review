package com.wangy.review.algorithm.sort.banana;

import java.util.Arrays;

/**
 * 归并排序的Java实现
 * <p>
 * 归并排序的思想是分治+递归。若要将数组排序，归并排序的思想就是将数组拆分为2个不同的数组(一般是从中拆分)，然后分别将2个数组排序，
 * 最后将已经排序的数组合并
 * </p>
 *
 * @author wangy
 * @version 1.0
 * @date 2021/7/6 / 19:46
 */
public class MergeSort {

    static void sort(int[] a) {
        int l = a.length;
        sort(a, 0, l - 1);
    }


    private static void sort(int[] a, int p, int r) {
        if (p >= r) return;
        int m = (p + r) / 2;
        // 分治
        sort(a, p, m);
        sort(a, m + 1, r);
        // 合并
        merge(a, p, m, r);
    }

    private static void merge(int[] a, int p, int m, int r) {
        int pos = 0;
        int left = p;
        int right = m + 1;
        // 需要额外的空间
        int[] tmpA = new int[r - p + 1];
        while (left <= m && right <= r) {
            if (a[left] > a[right]) {
                tmpA[pos++] = a[right++];
            } else {
                tmpA[pos++] = a[left++];
            }
        }
        // 分治数组是否有剩余元素，若有，全部添加到tmpA末尾
        while (left <= m) {
            tmpA[pos++] = a[left++];
        }
        while (right <= r) {
            tmpA[pos++] = a[right++];
        }
        // 将缓存数据写到原始数组中对应的位置
        // p, m, r
        for (int i = r - p; i >= 0; i--, r--) {
            a[r] = tmpA[i];
        }
        System.out.println("merge ---" + Arrays.toString(tmpA));
    }

    public static void main(String[] args) {
        int[] a = new int[]{3, 7, 2, 15, 9, 13, 1, 24, 77, 16};
        System.out.println(Arrays.toString(a));
        MergeSort.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
