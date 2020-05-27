package com.wangy.review.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author wangy
 * @version 1.0
 * @date 2018/9/25 / 10:23
 * @description 通过实现InvocationHandler来实现动态代理
 */
public class MyProxyHandler implements InvocationHandler {
    private Object target;

    MyProxyHandler(Object target) {
        this.target = target;
    }

    /**
     * 此方法用于生成一个指定接口的代理类实例, 该接口可以将方法调用指派到 [指定的调用处理程序],
     * 这个所谓的 [调用处理程序] 就是 InvocationHandler的invoke()方法
     *
     * @param subject 需要被代理的类的实例对象(被代理接口的实现类)
     * @return 一个指定接口的代理类实例
     * 这里, 指定接口就是说的是 Subject 接口, 常说的JDK动态代理需要有一个接口,就是这个原因
     */
    /**Object bind(Subject subject) {
     this.subject = subject;
     return Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject.getClass().getInterfaces(), this);
     // 此Proxy类的静态方法等价于
     //try {
     // 获取实现指定接口的被代理类实例对象
     //    Class<?> proxyClass = Proxy.getProxyClass(Subject.class.getClassLoader(), Subject.class.getInterfaces());
     // 获取指定的 [调用处理程序对象] 的构造器
     //    Constructor<?> proxyClassConstructor = proxyClass.getConstructor(MyProxyHandler.class);
     // 通过指定的InvocationHandler实例创建实现指定接口的代理类实例对象
     //    return proxyClassConstructor.newInstance(new MyProxyHandler(subject));
     //}catch(Exception e){
     //    e.printStackTrace();
     //}
     }*/

    /*  ************************************************
     *  java.lang.reflect.Proxy [extends Object implements Serializable] 类
     *      -- 该类提供用于创建动态代理类和实例的静态方法, 它是由这些静态方法创建的动态代理类的超类
     *      -- 动态代理类(以下称为代理类)是一个实现 [在创建类时在运行时指定的接口列表] 的类
     *      -- 代理接口(Subject)是代理类实现的一个接口
     *      -- 代理实例是代理类的一个实例, 每个代理实例都有一个关联的 [调用处理程序] 对象, 其实现接口 InvocationHandler
     *      -- 代理实例调用方法(sayHallo())时, 会被指派到 [调用处理程序] 对象的 invoke() 方法
     *  ************************************************/

    /**
     * @param proxy  代理类对象
     * @param method 被代理类的方法实例
     * @param args   被代理类对象(subject)的方法实例method的参数
     * @return null
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String name = method.getName();
        System.out.println("before...");
        Object invoke = method.invoke(target, args);
        System.out.println("after...");
        return null;
    }
}
