package com.wangy.review.references;

/**
 * 测试String对象的存储位置：堆/常量池
 *
 * 2022.06.08: JDK7开始，JVM已经将字符串常量池移到Java堆中
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/30 / 15:47
 */
@SuppressWarnings("all")
public class StringDemo {

    private void t1() {
        String a = "123";   // string pool
        String b = "123";   // string pool
        String c = new String("123");   // heap
        String d = new String("123");   // heap
        String e = a;
        String f = new String(a); // heap

        System.out.println(a == b); // true
        System.out.println(a == c); // false
        System.out.println(c == d); // false
        System.out.println(a == e); // true
        System.out.println(a == f); // false
        System.out.println(c == f); // false
    }

    private void t2() {
        String a = "str";
        String b = "ing";

        String c = "str" + "ing";   // string pool
        String d = a + b;   // heap
        String f = "string";    // string pool
        System.out.println(c == d); //false
        System.out.println(c == f); //true
    }

    void t3(){
        String a = "string";
        String b = new String("string");
        String c = b.intern();
        System.out.println(a == b); // false
        System.out.println(a == c); // true

    }

    public static void main(String[] args) {
        StringDemo sd = new StringDemo();
        sd.t1();
        System.out.println("=======");
        sd.t2();
    }
}
