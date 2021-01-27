package com.wangy.review.algorithm;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Write a function:
 *
 * <pre>
 * class Solution { public int solution(int[] A); }
 * </pre>
 * <p>
 * that, given an array A of N integers, returns the smallest positive integer (greater than 0)
 * that does not occur in A.
 * <p>
 * For example, given A = [1, 3, 6, 4, 1, 2], the function should return 5.
 * <p>
 * Given A = [1, 2, 3], the function should return 4.
 * <p>
 * Given A = [−1, −3], the function should return 1.
 * <p>
 * Write an efficient algorithm for the following assumptions:
 * <p>
 * N is an integer within the range [1..100,000];
 * <p>
 * each element of array A is an integer within the range [−1,000,000..1,000,000].
 * <p>
 * 此代码并未AC，只代表个人思路
 *
 * @author wangy
 * @version 1.0
 * @date 2020/12/2 / 16:33
 */
public class MinInt {


    public static void main(String[] args) throws FileNotFoundException {
        // file path: src/main/resources/txt/test-input.txt
        Scanner in = new Scanner(
            new FileInputStream(
                new File("src/main/resources/txt/test-input.txt")
            )
        );

        String str = in.nextLine();
        in.close();
        int s = str.indexOf("[") + 1;
        int e = str.indexOf("]");
        String arrStr = str.substring(s, e);
        String[] split = arrStr.split(",");
        int[] A = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            A[i] = Integer.parseInt(split[i]);
        }

        System.out.println(solution(A));

    }

    public static int solution(int[] A) {
        // write your code in Java SE 8
        Arrays.sort(A);
        Set<Integer> set = new TreeSet<>();
        int index = 0;

        if (A[0] < 0) {
            if ((index = Arrays.binarySearch(A, 0)) < 0) {
                int l = 0;
                int r = A.length - 1;
                int m = 0;
                while (l <= r) {
                    m = (l + r) / 2;
                    if (A[m] > 0) {
                        r = m - 1;
                    } else if (A[m] < 0) {
                        l = m + 1;
                    }
                }
                index = m;
                if (index == A.length - 1 && A[A.length - 1] < 0) return 1;
            }
        }

        for (int j = index; j < A.length; j++) {
            set.add(A[j]);
        }
        List<Integer> list = new ArrayList<>(set);

        if (list.get(0) > 1) return 1;
        int low = 0;
        int high = set.size() - 1;
        while (low != high - 1) {
            int mid = (low + high) / 2;

            if (list.get(mid) - list.get(low) > (mid - low)) {
                // consistence
                high = mid;
            } else if (list.get(high) - list.get(mid) > (high - mid)) {
                // un consistence
                low = mid;
            } else {
                low = high;
                break;
            }
        }
        return list.get(low) + 1;
    }
}
