package com.wangy.review.proxy.jdk;

/**
 * @author:wangy
 * @version:1.0
 * @date: 2018/9/25 / 10:22
 * @description: 被代理对象实体
 */
public class RealSubject implements Subject {
    @Override
    public void sayHallo() {
        System.out.println("大家好!");
    }

    @Override
    public int  showInfo() {
        System.out.println("showinfo()");
        return 1;
    }

    @Override
    public String toString() {
        return "toString()";
    }
}

