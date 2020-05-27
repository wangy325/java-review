package com.wangy.review.container.collection;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * simple test of array Deque
 * @author wangy
 * @version 1.0
 * @date 2020/4/30 / 17:25
 */
public class ArrayDequeTest {
    public static void main(String[] args) throws Exception{
        ArrayDequeTest test = new ArrayDequeTest();
        test.initializationTest();

    }

    void initializationTest() throws NoSuchFieldException, IllegalAccessException {
        Deque<Integer> aq = new ArrayDeque<>(5);
        // actual circle array size: 8
        System.out.println("array size : " + getElements(aq).length);
        for (int i = 0; i < 8; i++) {
            aq.offer(i);
        }
        Object[] elements = getElements(aq);
        System.out.println(Arrays.toString(elements));
        aq.addLast(19);
        aq.forEach(e-> System.out.print(e + "\t"));
    }

    private <T> T[] getElements(Deque<?> aq) throws NoSuchFieldException, IllegalAccessException {
        Class<?> cls = ArrayDeque.class;
        Field ef = cls.getDeclaredField("elements");
        ef.setAccessible(true);
        return  (T[]) ef.get(aq);
    }

    void asStack (){

        Deque<Integer> aq = new ArrayDeque<>();
        aq.push(1);
        aq.peek();
        aq.pop();
    }
}
