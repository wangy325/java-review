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
        bucketsTest();
//        testHash(7, "eh");
//        viewTest();
    }

    private static void viewTest() {
        Map<Integer, String> hm = new HashMap<>(8);
        hm.put(1, "难忘的一天");
        Set<Integer> keySet = hm.keySet();
        //keySet.add(2); // unsupported operation exception
        Iterator<Integer> ikey = keySet.iterator();
        ikey.next();
        // can remove key-value pair by keySet
        ikey.remove();
        ikey.forEachRemaining(System.out::println);

        Collection<String> values = hm.values();
        // already deleted
        System.out.println("values contains: " + values.contains("难忘的一天"));
        // values.add("你瞒我瞒"); // unsupported either
        hm.put(1, "你瞒我瞒");
        hm.put(2, "樱花树下");
        // ikey.next(); // fast-fail iterator, ikey is out of date
        boolean remove = values.remove("你瞒我瞒");
        Iterator<String> ivalue = values.iterator();
        ivalue.next();
        ivalue.remove();


        hm.put(1, "红豆");
        hm.put(2, "风衣");
        Set<Map.Entry<Integer, String>> entries = hm.entrySet();
        // entries.add() // unsupported either
        System.out.println("entry size: " + entries.size());
        // remove entry with particular key-value
        entries.remove(new Map.Entry<Integer, String>() {
            @Override
            public Integer getKey() {
                return 1;
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
        Iterator<Map.Entry<Integer, String>> ientry = entries.iterator();
        ientry.next();
        ientry.remove();
        ientry.forEachRemaining(System.out::println);

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
        Map.Entry<String,String>[] table = (Map.Entry<String, String>[]) t.get(hm);
        Map.Entry<String,String> node = table[7];
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
        hm.forEach((k, v) -> System.out.println(k + ", " + v));
        hm.put("apple", "music");
        // size > threshold, then resize happen
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
        int t;
        int hash = (key == null) ? 0 : (t = key.hashCode()) ^ (t >>> 16);
        int i = n & hash;
        // false
        System.out.println(i == n || i == hash);
    }

}
