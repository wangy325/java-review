package com.wangy.review.container.collection;

import java.lang.reflect.Field;
import java.util.*;

/**
 * illustrate initialization and usage of ArrayList
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/26 / 18:04
 */
public class ArrayListTest {

    private static List<String> a;

    static {
        a = new ArrayList<String>() {{
            add("apple");
            add("google");
            add("amazon");
            add("cisco");
            add("faceboog");
            add("twitter");
        }};
    }

    public static void main(String[] args) throws Exception {
//        initializeTest();
//        iteratorTest();
        subListTest();
//        listIteratorTest();
    }

    static void arraysCopyTest() {
        int[] a = new int[]{1, 2, 3, 4};
        int[] copy = Arrays.copyOf(a, a.length);
        copy[0] = 9;
        System.out.println(Arrays.toString(a));
        Integer[] b = new Integer[]{1, 2, 3, 4};
        Integer[] copy1 = Arrays.copyOf(b, b.length);
        copy1[0] = 9;
        System.out.println(Arrays.toString(b));
        b = Arrays.copyOf(b, 6);
        System.out.println(Arrays.toString(b));
        System.arraycopy(b, 1, b, 2, (4 - 1));
        System.out.println(Arrays.toString(b));
    }

    static void subListTest() {

        List<String> strings = a.subList(1, 2);
//        System.out.println(Arrays.toString(strings.toArray()));
        System.out.println("is subString instance of ArrayList? "
            + (strings instanceof ArrayList)
            + "\n-------");
//         a.add("iqoo"); // cause ConcurrentModifiedException for subList
        ListIterator<String> subIterator = strings.listIterator();
        while (subIterator.hasNext()) {
            subIterator.set(subIterator.next().toUpperCase() + " revised by subList");
        }
        subIterator.add("foobar added by subList");
        a.forEach(System.out::println);
        strings.clear();
        System.out.println("-------");
        a.forEach(System.out::println);
    }

    static void listIteratorTest() {
        ListIterator<String> listIterator = a.listIterator();
        listIterator.next();
        // do not change cursor
        listIterator.set("Apple");
        listIterator.previous();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }
        System.out.println("-------");
        // cursor changed
        listIterator.remove();
        listIterator.add("TWITTER"); // cursor in the end
        // reverse output
        while (listIterator.hasPrevious()) {
            System.out.println(listIterator.previous());
        }
    }


    static void iteratorTest() {
        a.forEach(System.out::println);
        System.out.println("------");
        Iterator<String> iterator = a.iterator();
        a.remove(2);

        // throw concurrentModifiedException
//        System.out.println(iterator.next());

        Iterator<String> newIterator = a.iterator();
        newIterator.next();
        newIterator.forEachRemaining(s -> {
            s = s.replaceFirst("^g", "G");
            System.out.println(s);
        });

    }

    static void initializeTest() throws NoSuchFieldException, IllegalAccessException {
        List<Integer> list = new ArrayList<>();
        // initial size = 10
        for (int i = 0; i < 20; i++) {
            list.add(new Random().nextInt(100));
            Field field = list.getClass().getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] o = (Object[]) field.get(list);
            System.out.println("size = " + (i + 1) + ", length = " + o.length + " ,element = " + Arrays.toString(o));
        }
    }
}
