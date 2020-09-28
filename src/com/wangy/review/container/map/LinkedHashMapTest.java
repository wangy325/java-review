package com.wangy.review.container.map;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/7 / 19:54
 */
public class LinkedHashMapTest {
    static Map<String, String> map = new LinkedHashMap<>(8);

    static {
        map.put("hebe", "不醉不会");
        map.put("andy", "谢谢你的爱");
        map.put("lala", "寻人启事");
        map.put("yoga", "成全");
    }

    public static void main(String[] args) {
//        insertOrderTest();
//        accessOrderTest();
//        viewTest();
//        eldestRemoveTest();
        lruCacheTest();
    }


    static void insertOrderTest() {
        Map<String, String> lhm = new LinkedHashMap<>(map);
        lhm.replace("lala", "浪费");
        System.out.println("entry in insertion order:");
        /*Set<Map.Entry<String, String>> entries = lhm.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + entry.getValue());
        }*/
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
        System.out.println("-------");
    }

    static void accessOrderTest() {
        // 使用访问顺序
        Map<String, String> lhm = new LinkedHashMap<>(8, 0.75f, true);
        lhm.putAll(map);
        System.out.println("entry in access order:");
        // 有效访问会将entry移动至队尾
        lhm.replace("yoga", "说谎");
        // 使用新值替换原键对应的值
        lhm.computeIfPresent("hebe", (k, v) -> "渺小");
        lhm.put("chua", "坠落");
        lhm.get("lala");
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
    }

    static void viewTest() {
        Map<String, String> lhm = new LinkedHashMap<>(8, 0.75f, true);
        lhm.putAll(map);
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
        Set<Map.Entry<String, String>> entries = lhm.entrySet();
        // 迭代器操作不会影响映射的排序
        // 因其没有调用LinkedHashMap的回调方法
        Iterator<Map.Entry<String, String>> i = entries.iterator();
        // 有序迭代
        for (Map.Entry<String, String> entry : entries) {
            entry.setValue("魔鬼中的天使");
            break;
        }
        System.out.println("------");
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
    }

    private static void eldestRemoveTest() {
        /**
         * 这个类设计一个容量为1的映射集
         * 如果不覆盖removeEldestEntry()方法，此方法返回false
         * removeEldestEntry()方法的返回值用于 {@link LinkedHashMap.afterNodeInsertion()}的判断条件
         */
        class Access<K, V> extends LinkedHashMap<K, V> {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                // 如果映射集中键值对数大于1，返回true
                return size() > 1;
            }
        }
        Access<Integer, String> access = new Access<>();
        // Access中始终只有一个条目
        access.put(1, "apple");
        access.put(2, "google");
        access.put(3, "facebook");
        access.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    /**
     * 此类用于实现一个计数器，50次随机访问之后将访问次数小于10的键值对删除
     */
    static void lruCacheTest() {
        class Cache<K, V> extends LinkedHashMap<K, V> {
            private final int count = 50;

            private Cache(int initialCapacity, float loadFactor, boolean accessOrder) {
                super(initialCapacity, loadFactor, accessOrder);
            }

            /**
             * 此方法总是返回false
             *
             * @param eldest
             * @return false
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                Set<Map.Entry<K, V>> entries = entrySet();
                AtomicInteger vs = new AtomicInteger();
                entries.removeIf(next -> {
                    K key = next.getKey();
                    V value = next.getValue();
                    if (value instanceof Integer) {
                        if ((Integer) value > 0) {
                            vs.addAndGet((Integer) value);
                            return (Integer) value < 10;
                        }
                    }
                    return false;
                });
                System.out.println(vs.intValue() == count);
                //若在此方法中对集合进行修改，那么必须返回false
                return false;
            }
        }
        Cache<Integer, Integer> cache = new Cache<>(8, 0.75f, true);
        // 初始化映射集， afterNodeInsertion
        cache.put(1, 0);
        cache.put(2, 0);
        cache.put(3, 0);
        cache.put(4, 0);
        cache.put(5, 0);
        for (int i = 0; i < cache.count; i++) {
            int key = new Random().nextInt(50) % 5 + 1;
            int value = cache.get(key);
            if (i == cache.count - 1) {
                //保证最后一次访问removeEldestEntry方法
                cache.remove(key);
            }
            // 将值增1，实现计数器效果
            // 此处不能使用compute方法，因此法会调用afterNodeInsertion
            // 设计的目的在最后一次put之后调用afterNodeInsertion方法，而使用compute会调用2次
//            cache.put(key, cache.compute(key, (k, v) -> Integer.sum(value, 1)));
            cache.put(key, ++value);

        }
        System.out.println("-------");

        cache.forEach((k, v) -> System.out.println(k + ": " + v));
    }
}
