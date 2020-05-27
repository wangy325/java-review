package com.wangy.review.container.map;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

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
        hm.put(1,"难忘的一天");
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
        hm.put(1,"你瞒我瞒");
        hm.put(2,"樱花树下");
        // ikey.next(); // fast-fail iterator, ikey is out of date
        boolean remove = values.remove("你瞒我瞒");
        Iterator<String> ivalue = values.iterator();
        ivalue.next();
        ivalue.remove();


        hm.put(1,"红豆");
        hm.put(2,"风衣");
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
        hm.forEach((k,v) -> System.out.println("key:" + k + ", value:" + v));
        Iterator<Map.Entry<Integer, String>> ientry = entries.iterator();
        ientry.next();
        ientry.remove();
        ientry.forEachRemaining(System.out::println);

    }

    static void bucketsTest() throws Exception {
        // initial capacity 8, load factor 0.75, threshold 6
        HashMap<String, String> hm = new HashMap<>(7);

        hm.put("1", "ok");
        hm.put("2", "fine");
        hm.put("3", "nice");
        hm.put("4", "no");
        hm.put("5", "ops");
        hm.put("6", "fuck");


        Class<?> cls = HashMap.class;

        Field table = cls.getDeclaredField("table");
        Field threshold = cls.getDeclaredField("threshold");
        Class<?> node = Class.forName("java.util.HashMap$Node");
        table.setAccessible(true);
        threshold.setAccessible(true);
        // Node<K,V>[]
        Object[] o = (Object[]) table.get(hm);
        System.out.println("initial buckets size: " + o.length);
        System.out.println("initial threshold: " + threshold.get(hm));

        Set<Map.Entry<String, String>> entries = hm.entrySet();
        System.out.println("number of entries: " + entries.size());
        //
        /*entries.forEach((e) -> {
            System.out.println(e.getKey() + e.getValue());
        });*/
        hm.forEach((k, v) -> System.out.println(k + ", " + v));
        hm.put("apple", "music");
        System.out.println(("buckets after rehash:" + ((Object[]) table.get(hm)).length));
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
