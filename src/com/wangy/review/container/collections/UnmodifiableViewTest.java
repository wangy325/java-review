package com.wangy.review.container.collections;

import java.util.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/9 / 09:31
 */
public class UnmodifiableViewTest {
    static List<String> l = new ArrayList<String>(){{
        add("fan");
        add("bar");
        add("foo");
        add("anchor");
        add("ripe");
        add("rope");
        add("hope");
    }};
    static Set<String> s = new HashSet<>(l);
    static Map<String,String> m = new HashMap<String, String>(){{
        put("c","cable");
        put("b","bar");
        put("f","floyd");
        put("e","echo");
        put("a","anchor");
        put("d","dribble");
    }};
    public static void main(String[] args) {
//        unmodifiableList();
//        unmodifiableSet();
        unmodifiableMap();
    }

    static void unmodifiableList(){
        List<String> strings = Collections.unmodifiableList(l);
        // strings.add("add"); // USOE
        // strings.remove("bar"); // USOE
        // strings.removeAll(l);// USOE
        strings.forEach(System.out::print);
        ListIterator<String> iterator = strings.listIterator();
        System.out.println(iterator.nextIndex());
        List<String> strings1 = strings.subList(1, 2);
        strings1.forEach(System.out::println);
    }

    static void unmodifiableSet(){
        Set<String> set = Collections.unmodifiableSet(s);
        System.out.println(set.contains("anchor"));
        Iterator<String> i = set.iterator();
        i.next();
        // i.remove(); //USOE
        // set.clear(); // USOE
        TreeSet<String>  ts = new TreeSet<>(s);
        // 使用sorted set构建
        NavigableSet<String> strings = Collections.unmodifiableNavigableSet(ts);
        // String s = strings.pollFirst(); //USOE
        System.out.println(strings.first());
        NavigableSet<String> anchor = strings.headSet("anchor", true);
        //anchor.remove("anchor"); //USOE
        anchor.forEach(System.out::println);
    }

    static void unmodifiableMap(){
        Map<String, String> map = Collections.unmodifiableMap(m);
        // map.replace("a","apple"); //USOE
        Set<Map.Entry<String, String>> e = map.entrySet();
        System.out.println(map.get("f"));
        Iterator<Map.Entry<String, String>> i = e.iterator();
        i.next();
        // i.remove(); //USOE
        TreeMap<String,String> tm = new TreeMap<>(m);
        // 使用sorted map
        NavigableMap<String, String> nm = Collections.unmodifiableNavigableMap(tm);
        System.out.println(nm.ceilingEntry("car").getValue());
        NavigableMap<String, String> sm = nm.subMap("b", true, "d", true);
        // sm.remove("c"); //USOE
        sm.forEach((k,v)-> System.out.println(k+", "+v));
        NavigableMap<String, String> descendingMap = sm.descendingMap();
        descendingMap.forEach((k,v)-> System.out.println(k+", "+v));
    }
}
