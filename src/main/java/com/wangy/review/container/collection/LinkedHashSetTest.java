package com.wangy.review.container.collection;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * a simple linked hash set test
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/6 / 11:28
 */
public class LinkedHashSetTest {
    public static void main(String[] args) {
        iteratorTest();
    }

    static void iteratorTest(){
        Set<String> lhs = new LinkedHashSet<String>(10){{
            add("arrayList");
            add("linkedList");
            add("priorityQueue");
            add("arrayDeque");
            add("hashSet");
        }};
        // insert order
        for (String lh : lhs) {
            System.out.println(lh);
        }
    }
}
