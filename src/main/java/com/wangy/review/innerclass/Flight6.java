package com.wangy.review.innerclass;

import java.util.ArrayList;
import java.util.List;

/**
 * anonymous inner class extends class
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/20 / 10:01
 */
public class Flight6 {
    public static void main(String[] args) {
        Pistol p = new Pistol();
        Wrap wrap = p.wrap(3);
        System.out.println(wrap.value());

        String[] strings = {"ali","google","amazon"};

        // {{}} initialization
        List<String> list = new ArrayList<String>(){{add("ali"); add("google"); add("amazon");}};
    }
}

class Pistol {
    Wrap wrap(int x) {
        return new Wrap(x) {
            int v;
            // 构造代码块
            {
                System.out.println("extended initialized");
                v = super.value() * 3;
            }

            @Override
            int value() {
                return v;
            }
        };
    }
}

class Wrap {
    private int i;

    public Wrap(int i) {
        this.i = i;
        System.out.println("base constructor");
    }

    int value() {
        return i;
    }
}
