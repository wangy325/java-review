package com.wangy.review.defaultwords.sub;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/13 / 16:22
 */
 public class C {
     int a ;
     private int b;

    public C(int a, int b) {
        this.a = a;
        this.b = b;
        System.out.println("C constructed");
    }

    protected void print_b(){
        System.out.println(this.b);
    }
}
