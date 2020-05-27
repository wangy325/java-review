package com.wangy.review.classandinterface;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/16 / 09:57
 */
public class AbsTest {
    public static void main(String[] args) {
        S s = new S();
        s.f();
        s.g();
    }
}

 abstract class F{
    public F() {
        System.out.println("F constructor");
    }

    abstract void f();

    void g(){
        System.out.println("F.g()");
    }
}

class S extends F{
    public S() {
        super();
        System.out.println("S constructor");
    }

    @Override
    void f() {
        System.out.println("S.f()");
    }
}
