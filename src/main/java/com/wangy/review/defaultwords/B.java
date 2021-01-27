package com.wangy.review.defaultwords;

import com.wangy.review.defaultwords.sub.C;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/13 / 16:14
 */
 class B extends C{

    public B(int a, int b) {
        super(a, b);
    }

    @Override
    protected void print_b(){
        super.print_b();
        System.out.println("b print_b");
    }
    public static void main(String[] args) {
        A a = new A(1,2,3);
        a.print_b();

        B b = new B(4,5);

        b.print_b();
    }
}
