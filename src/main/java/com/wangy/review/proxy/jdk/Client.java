package com.wangy.review.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * @author:wangy
 * @version:1.0
 * @date: 2018/9/25 / 10:30
 * @description:
 */

public class Client {

    public static void main(String[] args) {
        MyProxyHandler handler = new MyProxyHandler(new RealSubject());
        Subject instance = (Subject) Proxy.newProxyInstance(
            RealSubject.class.getClassLoader(),
            new Class[]{Subject.class},
            handler);
        int i = instance.showInfo();

    }


}
