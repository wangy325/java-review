package com.wangy.review.classandinterface;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/16 / 12:42
 */
public class InterTest {
    public static void main(String[] args) {
        System.out.println(I.get());
        I i = new L();
        i.j();
        i.k();
    }
}

interface I {
    int CONSTANT = 1;

    static int get() {
        return CONSTANT;
    }

    void j();

    default void k() {
        System.out.println("I.k()");
    }
}

class L implements I {

    @Override
    public void j() {
        System.out.println("L.j()");
    }
}