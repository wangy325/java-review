package com.wangy.review.container.collection;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * illustrate the initialization and usage of LinkedList
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/28 / 00:50
 */
public class LinkedListTest {

    public static void main(String[] args) throws Exception {
//        initializeTest();
//        iteratorTest();
        dequeTest();
    }

    static void initializeTest() throws Exception {
        List<String> a = new LinkedList<>();
        a.add("google");
        a.add("chrome");
        a.add("photos");

        Class<?> cls = LinkedList.class;
        // LinkedList field
        Field ff = cls.getDeclaredField("first");
        Field lf = cls.getDeclaredField("last");
        ff.setAccessible(true);
        lf.setAccessible(true);
        Object first =  ff.get(a);
        Object last = lf.get(a);
        Class<?> node = Class.forName("java.util.LinkedList$Node");
        // LinkedList$Node field
        Field item = node.getDeclaredField("item");
        Field next = node.getDeclaredField("next");
        Field prev = node.getDeclaredField("prev");
        item.setAccessible(true);
        next.setAccessible(true);
        prev.setAccessible(true);
        // first
        System.out.println("first: " + first);
        Object firstItem = item.get(first);
        Object firstPrev = prev.get(first); // Node
        Object firstNext = next.get(first); // Node
        System.out.println("\t" + "item: " + firstItem +"\n\t" +
                            "prev: " + firstPrev + "\n\t" +
                            "next: " + firstNext + "\n");
        // last
        System.out.println("last: " + last);
        Object lastItem = item.get(last);
        Object lastPrev = prev.get(last);
        Object lastNext = next.get(last);
        System.out.println("\t" + "item: " + lastItem +"\n\t" +
            "prev: " + lastPrev + "\n\t" +
            "next: " + lastNext);
    }

    static void iteratorTest(){
        List<String> list = new LinkedList<String>(){{
            add("Java");
            add("Python");
            add("JavaScript");
            add("C");
        }};

        ListIterator<String> i = (ListIterator<String>) list.iterator();
        while (i.hasNext()){
            if (i.next().equals("JavaScript")){
                i.set("JS");
            }
        }
        i.remove();
        i.add("C++");
        while (i.hasPrevious()){
            System.out.println(i.previous());
        }
        System.out.println("-------");
        ListIterator<String> iterator = list.listIterator(2);
        iterator.forEachRemaining(System.out::println);
    }

    static void dequeTest(){
        Deque<String> ll = new LinkedList<>();
        // LinkedList can use as deque
        ll.add("google");
        ll.addFirst("apple");
        ll.forEach(System.out::println);
        ll.offerLast("microsoft");
        Iterator<String> iterator = ll.iterator();
        iterator.next();
        iterator.remove();
        ll.forEach(System.out::println);
    }
}
