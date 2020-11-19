package com.wangy.review.finalwords;

/**
 * 本类测试java静态常量-编译时常量
 *
 * @author wangy
 * @version 1.0
 * @date 2020/9/21 / 06:28
 */

public class Constant {
    public static void main(String[] args) {
        System.out.println("NUM = " + ConstantT.NUM);
        System.out.println("b = " + ConstantT.b);
        System.out.println("WORDS = " + ConstantT.WORDS);
        System.out.println("LEN = " + ConstantT.LEN);
    }
}

class ConstantT {
    /**编译时常量*/
    static final int NUM = 10;
    static final String WORDS = "hello";
    /**运行时常量*/
    static final int LEN = WORDS.length();
    static  final int  b ;

    // 静态代码块在类加载（非实例化）时执行
    //编译时常量不依赖类，而运行时常量依赖类
    static  {
        System.out.println("class loaded..");
        b = 10;
    }

}
