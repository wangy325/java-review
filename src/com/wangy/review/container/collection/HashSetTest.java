package com.wangy.review.container.collection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * simple hashSet test
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/3 / 10:28
 */
public class HashSetTest {

    public static void main(String[] args) throws Exception {
//        initializationTest();
//        hashTest("obj");
        contentTest();
    }

    static void initializationTest() throws Exception {
        Set<Integer> hs = new HashSet<>();

        hs.add(1);
        hs.add(2);

        Class<?> cls = HashSet.class;

        Field fm = cls.getDeclaredField("map");
        fm.setAccessible(true);
        System.out.println(fm.get(hs).getClass());
        @SuppressWarnings("unchecked")
        HashMap<Integer, Object> o = (HashMap<Integer, Object>) fm.get(hs);
        System.out.println(o.get(1).getClass());
    }

    static void hashTest(Object key) {
        int i;
        i = (19 - 1) & key.hashCode();
        System.out.println(i);
        System.out.println("\t" + key.hashCode());
        System.out.println("^");
        System.out.println("\t" + (key.hashCode() >>> 16));
        System.out.println("------------");
        System.out.println("\t" + (key.hashCode() ^ key.hashCode() >>> 16));
    }

    /**
     * illustrate that <code>a ^ b</code> equals to <code>(~a & b) | (a & ~b)</code>
     *
     * @param a
     * @param b
     * @return
     */
    static boolean xorEqualsNorAnd(int a, int b) {
        //System.out.println(a ^ a); // always 0
        return (a ^ b) == ((~a) & b | a & (~b));
    }

    static void contentTest(){
        Set<Object> hs = new HashSet<>(7);

        class Item{
            private int code;
            private String name;

            private Item(int code, String name) {
                this.code = code;
                this.name = name;
            }

            @Override
            public String toString() {
                return "Item{" +
                    "code=" + code +
                    ", name='" + name + '\'' +
                    '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }

                Item item = (Item) o;

                if (code != item.code) {
                    return false;
                }
                return Objects.equals(name, item.name);
            }

            @Override
            public int hashCode() {
                int result = code;
                result = 31 * result + (name != null ? name.hashCode() : 0);
                return result;
            }
        }
        hs.add(new Item(12,"apple"));
        hs.add(new Item(1,"apple"));
        hs.add(new Item(13,"apple"));
        hs.add(new Item(21,"apple"));
        hs.forEach(System.out::println);

    }

}
