package com.wangy.review.references;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 15:15
 */
@SuppressWarnings("all")
class ByValue {
    static void tripleValue(Integer x) {
        x = 3 * x;
        System.out.println("x = " + x);
    }

    public static void main(String[] args) {
        Integer y = 10;
        y = 3 * y;
        tripleValue(y);
        System.out.println("y = " + y);
        Integer a = 17;
        Integer b = 17;
        Integer c = new Integer(17);
        Integer d = new Integer(17);
        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(c == d);
    }
}
