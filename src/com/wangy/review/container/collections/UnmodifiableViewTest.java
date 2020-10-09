package com.wangy.review.container.collections;

import java.util.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/9 / 09:31
 */
public class UnmodifiableViewTest {
    static List<String> l = new ArrayList<String>() {{
        add("fan");
        add("bar");
        add("foo");
        add("anchor");
        add("ripe");
        add("rope");
        add("hope");
    }};
    static Set<String> s = new HashSet<>(l);
    static Map<String, String> m = new HashMap<String, String>() {{
        put("c", "cable");
        put("b", "bar");
        put("f", "floyd");
        put("e", "echo");
        put("a", "anchor");
        put("d", "dribble");
    }};

    public static void main(String[] args) {
//        unmodifiableList();
//        unmodifiableSet();
        unmodifiableMap();
    }

    static void unmodifiableList() {
        List<String> ul = Collections.unmodifiableList(l);
        // 对视图集的元素增删会抛出UnsupportedOperationException
        // strings.add("add");
        // strings.remove("bar");
        // strings.removeAll(l);
        ul.forEach(System.out::print);
        //可以操作迭代器
        ListIterator<String> iterator = ul.listIterator();
        System.out.println(iterator.nextIndex());
//        List<String> ul_sub = l.subList(1, 3);
        List<String> ul_sub = ul.subList(1, 3);
        // 子集对元素的操作也是不支持的
//        ul_sub.removeIf(s -> s.equals("foo"));
        ul_sub.forEach(System.out::println);
    }

    static void unmodifiableSet() {
        Set<String> set = Collections.unmodifiableSet(s);
        System.out.println(set.contains("anchor"));
        Iterator<String> i = set.iterator();
        i.next();
        // 迭代器无法移除元素是必然的
        // i.remove();
        // set.clear();
        TreeSet<String> ts = new TreeSet<>(s);
        // 使用sorted set构建
        NavigableSet<String> ns = Collections.unmodifiableNavigableSet(ts);
        // 无法从集中移除元素 UnsupportedOperationException
//        String s = ns.pollFirst();
        System.out.println(ns.first());
        NavigableSet<String> anchor = ns.headSet("anchor", true);
        // 子集也不能被修改
//        anchor.remove("anchor");
        anchor.forEach(System.out::println);
    }

    static void unmodifiableMap() {
        Map<String, String> map = Collections.unmodifiableMap(m);
        // 不支持的操作
        // map.replace("a","apple");
        Set<Map.Entry<String, String>> e = map.entrySet();
        System.out.println(map.get("f"));

        TreeMap<String, String> tm = new TreeMap<>(m);
        // 使用sorted map
        NavigableMap<String, String> nm = Collections.unmodifiableNavigableMap(tm);
        System.out.println(nm.ceilingEntry("car").getValue());
        NavigableMap<String, String> sm = nm.subMap("b", true, "d", true);
        // 不支持的操作
        // sm.remove("c");
        sm.forEach((k, v) -> System.out.println(k + ", " + v));
        NavigableMap<String, String> descendingMap = sm.descendingMap();
        descendingMap.forEach((k, v) -> System.out.println(k + ", " + v));
    }
}
