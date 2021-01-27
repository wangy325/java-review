package com.wangy.review.classandinterface;

/**
 * conflict between interface default method and abstract method
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/16 / 16:17
 */
public class InterfaceTest {
    public static void main(String[] args) {
        C c = new C();
        c.di();
    }
}

interface I1 {
    default void di() {
        System.out.println("I1.di()");
    }
}

interface I2 {
    void di();
}

class C implements I1, I2 {

    @Override
    public void di() {
        // use implication of I1
        System.out.println("calling C.di()");
        I1.super.di();
    }
}