package com.wangy.review.container.map;

import java.util.*;


/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/7 / 19:54
 */
public class LinkedHashMapTest {
    static Map<String, String> map = new HashMap<>(8);

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
        lhm.replace("yoga", "说谎");
        System.out.println("entry in insertion order:");
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
        System.out.println("-------");
    }

    static void accessOrderTest() {
        Map<String, String> lhm = new LinkedHashMap<>(8, 0.75f, true);
        lhm.putAll(map);
        System.out.println("entry in access order:");
        // 有效访问会将entry移动至队尾
        lhm.replace("yoga", "说谎");
        lhm.computeIfPresent("hebe", (k, v) -> "魔鬼中的天使");
        lhm.put("chua", "坠落");
        lhm.get("lala");
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
    }

    static void viewTest() {
        Map<String, String> lhm = new LinkedHashMap<>(8, 0.75f, true);
        lhm.putAll(map);
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
        Set<Map.Entry<String, String>> entries = lhm.entrySet();
        // 视图操作不会影响映射的排序
        Iterator<Map.Entry<String, String>> i = entries.iterator();
        for (Map.Entry<String, String> entry : entries) {
            entry.setValue("魔鬼中的天使");
            break;
        }
        System.out.println("------");
        lhm.forEach((k, v) -> System.out.println("\t" + k + ": " + v));
        i.next();
    }

    private static void eldestRemoveTest() {
        class Access<K, V> extends LinkedHashMap<K, V> {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > 1;
            }
        }
        Access<Integer, String> access = new Access<>();
        access.put(1, "apple");
        // Access中始终只有一个条目
        access.put(2, "google");
        access.put(3,"facebook");
        access.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    static void lruCacheTest() {
        class Cache<K, V> extends LinkedHashMap<K, Integer> {
            private int count;

            private Cache(int initialCapacity, float loadFactor, boolean accessOrder) {
                super(initialCapacity, loadFactor, accessOrder);
                this.count = 0;
            }

            @Override
            public Integer get(Object key) {
//                Integer value = super.get(key);
                Integer value = remove(key);
                put((K) key, ++value);
                return value;
            }

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, Integer> eldest) {
                count++;
                if (count == 55) {
                    Set<Map.Entry<K, Integer>> entries = entrySet();
                    // 典型的在forEach中使用集合方法出现CME异常的情形
                    /*for (Map.Entry<K, Integer> entry : entries) {
                        if (entry.getValue() < 8) {
                            System.out.println(entry.getKey() + ": " + entry.getValue());
                            // entries.remove(entry); // may cause CME
                        }
                    }*/
                    entries.removeIf(next -> {
                        System.out.println(next.getKey() + ": " + next.getValue());
                        return next.getValue() < 10;
                    });
                }
                //若在此方法中对集合进行修改，那么必须返回false
                return false;
            }
        }
        Cache<Integer, Integer> cache = new Cache<Integer, Integer>(8, 0.75f, true);
        cache.put(1, 0);
        cache.put(2, 0);
        cache.put(3, 0);
        cache.put(4, 0);
        cache.put(5, 0);
        for (int i = 0; i < 50; i++) {
            cache.get(new Random().nextInt(50) % 5 + 1);
        }
        System.out.println("------");
        cache.forEach((k, v) -> System.out.println(k + ": " + v));
    }


}
