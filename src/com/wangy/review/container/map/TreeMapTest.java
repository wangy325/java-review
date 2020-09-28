package com.wangy.review.container.map;

import java.util.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/8 / 12:05
 */
public class TreeMapTest {
    static Map<String, String> map = new HashMap<>(8);

    static {
        map.put("hebe", "不醉不会");
        map.put("AMIT", "母系社会");
        map.put("Lin", "可惜没如果");
        map.put("andy", "一起走过的日子");
        map.put("lala", "寻人启事");
        map.put("yoga", "说谎");
    }

    public static void main(String[] args) {
//        treeMapTest();
        navigableTest();
    }

    static void treeMapTest() {
        Map<String, String> tm = new TreeMap<>(map);
        Set<Map.Entry<String, String>> entries = tm.entrySet();
        tm.put("andy", "来生缘"); // 映射和entrySet是互相作用的
        for (Map.Entry<String, String> entry : entries) {
            entry.setValue("难搞");
            break;
        }
        tm.computeIfPresent("lala", (k, v) -> "失落沙洲");
        // entries.add(new Map.Entry<String, String>() {...}); // Unsupported Operation Exception
        // 字典序迭代
        tm.forEach((k, v) -> System.out.println(k + ": " + v));

        // test iterator
        Iterator<Map.Entry<String, String>> ie = entries.iterator();
        ie.next();
        ie.remove();
        // tm.putAll(map); // ConcurrentModificationException
        ie.next();
        ie.forEachRemaining(x -> System.out.print(x + "\t"));
        // 指定比较器
        Map<String, String> tm2 = new TreeMap<>(String::compareToIgnoreCase);
        tm2.putAll(map);
        System.out.println();
        tm2.forEach((k, v) -> System.out.println(k + ": " + v));

    }

    static void navigableTest() {
        TreeMap<String, String> tm = new TreeMap<>(map);
        System.out.println(tm.firstEntry().getKey());
        // 使用一个比key 'andy'大的值，即可包含这个key，"+ 0"是一个实用手段
        SortedMap<String, String> subMap = tm.subMap("AMIT", "andy" + "0");
        subMap.compute("AMIT", (k, v) -> "彩虹");
        subMap.forEach((k, v) -> System.out.println(k + ", " + v));

        // NavigableMap接口方法，返回大于或等于给定key的一个entry
        System.out.println(tm.ceilingEntry("AMIT").getValue());

    }
}
