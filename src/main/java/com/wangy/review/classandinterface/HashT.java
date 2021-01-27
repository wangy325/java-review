package com.wangy.review.classandinterface;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/15 / 18:47
 */
public class HashT {
    public static void main(String[] args) {
        String s = "s";
        String t = new String("s");
        StringBuilder sb = new StringBuilder(s);
        StringBuilder tb = new StringBuilder(t);
        System.out.println(s.hashCode() + " : " +sb.hashCode());
        System.out.println(t.hashCode() + " : " +tb.hashCode());
    }
}
