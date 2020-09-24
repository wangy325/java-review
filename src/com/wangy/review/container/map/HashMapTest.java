package com.wangy.review.container.map;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/6 / 22:32
 */
public class HashMapTest {
    public static void main(String[] args) throws Exception {
//        bucketsTest();
        testHash(7, "eh");
//        viewTest();
    }

    private static void viewTest() {
        Map<Integer, String> hm = new HashMap<>(8);
        hm.put(1, "难忘的一天");
        Set<Integer> keySet = hm.keySet();
//        keySet.add(2); // UnsupportedOperationException
        Iterator<Integer> ikey = keySet.iterator();
//        hm.put(19, "难忘的一天"); // ConcurrentModificationException
        ikey.next();
        // can remove key-value pair by keySet
        ikey.remove();
        ikey.forEachRemaining(System.out::println);

        // already deleted, no key-value pair in Map now
        Collection<String> values = hm.values();
        /**
         * 映射条目很多时，这个方法效率不高
         * @see  java.util.HashMap#containsValue(java.lang.Object)
         */
        System.out.println("contains '难忘的一天': " + values.contains("难忘的一天"));
        // values.add("你瞒我瞒"); // UnsupportedOperationException
        hm.put(3, "你瞒我瞒");
        hm.put(4, "樱花树下");
//         ikey.next(); //ConcurrentModificationException, fast-fail iterator, ikey is out of date
        boolean remove = values.remove("你瞒我瞒");
        Iterator<String> ivalue = values.iterator();
        ivalue.next();
        ivalue.remove();

        // map should be empty now

        hm.put(5, "红豆");
        hm.put(6, "风衣");
        Set<Map.Entry<Integer, String>> entries = hm.entrySet();
//         entries.add() // UnsupportedOperationException
        System.out.println("entry size: " + entries.size());
        // remove entry with particular key-value
        entries.remove(new Map.Entry<Integer, String>() {
            @Override
            public Integer getKey() {
                return 5;
            }

            @Override
            public String getValue() {
                return "红豆";
            }

            @Override
            public String setValue(String value) {
                return null;
            }
        });
        hm.forEach((k, v) -> System.out.println("key:" + k + ", value:" + v));
        Iterator<Map.Entry<Integer, String>> ie = entries.iterator();
//        hm.put(7, "再度重相逢"); // ConcurrentModificationException
        ie.next();
        ie.remove();
        // empty now
        ie.forEachRemaining(System.out::println);

    }

    static void bucketsTest() throws Exception {
        // default load factor 0.75,
        HashMap<String, String> hm = new HashMap<>(8);

        hm.put("ok", "ok");
        hm.put("fine", "fine");
        hm.put("nice", "nice");
        hm.put("no", "no");
        hm.put("oops", "oops");
        hm.put("fuck", "fuck");


        Class<?> cls = HashMap.class;

        Field t = cls.getDeclaredField("table");
        Field threshold = cls.getDeclaredField("threshold");
        // 不能使用Hash.Node对象
//        Class<?> Node = Class.forName("java.util.HashMap$Node");
        t.setAccessible(true);
        threshold.setAccessible(true);
        // Node<K,V>[]
        Map.Entry<String, String>[] table = (Map.Entry<String, String>[]) t.get(hm);
        Map.Entry<String, String> node = table[7];
        // illegal
//        Map.Entry<String,String> next = node.next;
//        System.out.println(next.getKey() + ":" + next.getValue());
        System.out.println("initial capacity: " + table.length);
        System.out.println("initial threshold: " + threshold.get(hm));
        System.out.println("size before: " + hm.size());

        //传统遍历方法
        /*Set<Map.Entry<String, String>> entries = hm.entrySet();
        entries.forEach((e) -> {
            System.out.println(e.getKey() + e.getValue());
        });*/
        hm.forEach((k, v) -> System.out.print(k + "," + v + ";\t"));
        hm.put("apple", "music");
        // size > threshold, then resize happen
        System.out.println();
        System.out.println(("capacity after resize:" + ((Object[]) t.get(hm)).length));
        System.out.println("threshold after resize: " + threshold.get(hm));
        System.out.println("size after: " + hm.size());
    }

    static void testHash(int cap, Object key) {
        int max = 1 << 30;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n = (n < 0) ? 1 : Math.min(n, max);
        n++;
        int t = key.hashCode() + (1<< 16);
        // 对于任何小于1<<16的正数t，做t>>>16运算，得0；任何非0数n，有n^0=n
        System.out.println(t< (1<<16) && (t >>> 16) ==0);
        // HashMap中为何如此计算key的hash值？
        int hash = (key == null) ? 0 : (t ^ (t >>> 16));
        int i = (n - 1) & hash;
        // false
        System.out.println(i == n || i == hash);
    }

}
