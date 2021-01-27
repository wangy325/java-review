package com.wangy.review.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 2分查找
 *
 * @author wangy
 * @version 1.0
 * @date 2020/6/5 / 14:23
 */
public class BinSearch {

    public static int binSearch(ArrayList<Integer> list, int key) {
        Collections.sort(list);
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (list.get(mid) > key) {
                high = mid - 1;
            }
            else if (list.get(mid) < key) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;

    }

    public static void main(String[] args) {
        ArrayList<Integer> al = new ArrayList<Integer>() {{
            add(3);
            add(43);
            add(23);
            add(83);
            add(33);
            add(63);
            add(93);
            add(103);
            add(53);
        }};
        Collections.sort(al);
        System.out.println(Arrays.toString(al.toArray()));

        int i = BinSearch.binSearch(al, 33);
        System.out.println(i);

        System.out.println(Collections.binarySearch(al,33));
    }
}
