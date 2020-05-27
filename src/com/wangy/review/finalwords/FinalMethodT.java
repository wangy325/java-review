package com.wangy.review.finalwords;

/**
 * simple method can prevent it extends by subclass
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 22:33
 */
public class FinalMethodT {
    public static void main(String[] args) {
        FinalMethodExt x = new FinalMethodExt();
        x.f();
        x.g();
        x.p();
        System.out.println("----");
        // upcast
        FinalMethod y = x;
        y.f();
        y.g();
        // y.p() // can't access
        System.out.println("----");
        FinalMethod z = new FinalMethod();
        z.f();
    }
}

class FinalMethod {
    void f() {
        System.out.println("f()");
    }

    final void g() {
        System.out.println("g()");
    }

    private final void p() {
        System.out.println("p()");
    }
}

class FinalMethodExt extends FinalMethod {

    void f() {
        super.f();
        System.out.println("ext f()");
    }
    // cannot override
    // final void g(){ System.out.println("ext g()"); }

    final void p() {
        System.out.println("ext p()");
    }
}
