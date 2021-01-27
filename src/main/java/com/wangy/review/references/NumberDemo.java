package com.wangy.review.references;

/**
 * 测试基本数据类型的存储：堆/常量池; 缓存数据在池中，缓存范围[-128~127]
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/30 / 16:36
 */
@SuppressWarnings("all")
public class NumberDemo {

    private void t1(){
        Integer i1 = 33;
        Integer i2 = 33;
        System.out.println(i1 == i2); // true
        Integer i11 = 128;
        Integer i22 = 128;
        System.out.println(i11 == i22);	// false
        // 浮点数据没有使用缓存池技术
        Double i3 = 1.2;
        Double i4 = 1.2;
        System.out.println(i3 == i4); // false
    }

    public static void main(String[] args) {
        NumberDemo nd = new NumberDemo();
        nd.t1();
    }
}
