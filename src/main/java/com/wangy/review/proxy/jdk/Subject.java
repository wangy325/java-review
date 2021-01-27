package com.wangy.review.proxy.jdk;

/**
 * @author  wangy
 * jdk 动态代理类
 */
public interface Subject {
    /**
     * 1.创建一个实现接口InvocationHandler的类，它必须实现invoke方法
     * 2.创建被代理的类以及接口
     * 3.通过Proxy的静态方法
     * newProxyInstance(ClassLoaderloader, Class[] interfaces, InvocationHandler h)创建一个代理
     * 4.通过代理调用方法
     */
    void sayHallo();

    int showInfo();
}
