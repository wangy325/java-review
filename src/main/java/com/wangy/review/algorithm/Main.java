package com.wangy.review.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/12/9 / 15:22
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(sln2("123"));
    }

    static int sln(int n) {

        String is = String.valueOf(Math.abs(n));
        StringBuilder rs = new StringBuilder();
        int mid = 5;
        boolean add = false;
        for (int i = 0; i < is.length(); i++) {
            int v = Integer.parseInt(String.valueOf(is.charAt(i)));
            if (n >= 0) {
                if (v <= mid && !add) {
                    rs.append(mid);
                    add = true;
                }
            } else {
                if (v >= mid && !add) {
                    rs.append(mid);
                    add = true;
                }
            }
            rs.append(v);
        }
        if (rs.length() == is.length()) rs.append(mid);
        return n >= 0 ? Integer.parseInt(rs.toString()) : -1 * Integer.parseInt(rs.toString());
    }

    static int sln2(String s) {
        int count = 0;
        List<String> sl = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                if (sb.length() > 1) {
                    sl.add(sb.toString());
                }
                sb.delete(0, sb.length());
            } else {
                sb.append(s.charAt(i));
                count++;
            }
        }
        int r = 0;
        for (int i = 0; i < sl.size(); i++) {
            int l = sl.get(i).length();
            while (l > 0) {
                r += (l - 1);
            }
        }
        return count + r;
    }
}
