package com.wangy.review.container.collections;


import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/9 / 15:26
 */
public class EmptyTest {

    public static void main(String[] args) {
//        emptyList();
//        singletonList();
    }

    static void emptyList() {
        List<Object> emptyList = Collections.emptyList();
//        emptyList.add(1); // USOE
        System.out.println(emptyList.size());
    }

    static void singletonList() {
        Set<String> singlton = Collections.singleton("singlton");
        System.out.println(singlton.size());
        // singlton.add("sin"); // USOE
        // singlton.clear(); // USOE
    }
}
