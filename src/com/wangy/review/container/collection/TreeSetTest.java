package com.wangy.review.container.collection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/6 / 14:35
 */
public class TreeSetTest {
    public static void main(String[] args) {
//        consistenceTest();
        eleTest();
    }

    static void consistenceTest() {

        class Item implements Serializable {
            private int code;
            private String name;

            public Item(int code, String name) {
                this.code = code;
                this.name = name;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Item item = (Item) o;

                if (code != item.code) return false;
                return Objects.equals(name, item.name);
            }

            @Override
            public int hashCode() {
                int result = code;
                result = 31 * result + (name != null ? name.hashCode() : 0);
                return result;
            }
        }
        // 故意修改比较器的相等逻辑，可以看到Set中有2个相等对象
        // 所以在编程中应该保证TreeSet中元素的equals方法和比较器的逻辑一致
        SortedSet<Item> ss = new TreeSet<>((o1, o2) -> o1.code - o2.code + 1);
        Item item = new Item(1, "apple");
        ss.add(item);
        ss.add(item);
        ss.forEach(System.out::println);
    }

    static void eleTest() {
        TreeSet<String> ss = new TreeSet<String>() {{
            add("nokia");
            add("motorola");
            add("apple");
            add("samsung");
            add("mi");
            add("oppo");
            add("vivo");
            add("sony");
            add("google");
        }};

        // 子集
        SortedSet<String> headSet = ss.headSet("oppo", false);
        ss.add("huawei");
        Iterator<String> i = headSet.iterator();
        int j = 0;
        while (i.hasNext()) {
            j++;
            i.next();
            if (j % 2 == 0) {
                i.remove();
            }
        }
        // 对子集的修改作用于原集上，google已被删除
        ss.forEach(System.out::println);
        System.out.println("contains google? " + ss.contains("google"));
        Iterator<String> i2 = ss.descendingIterator();
        // 元素有序，vivo在最后
        System.out.println("vivo".equals(i2.next()));
    }
}
