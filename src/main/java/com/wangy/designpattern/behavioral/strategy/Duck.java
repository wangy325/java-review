package com.wangy.designpattern.behavioral.strategy;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:21
 */
public abstract class Duck {

    protected FlyBehavior flyBehavior;
    protected QuarkBehavior quarkBehavior;

    // 不变的部分
    public abstract void swim();

    public abstract void display();

    // fly and quark
    // 由于并不是所有的"鸭子"实现不能都会飞或者叫（实际开发中经常遇到实现并不需要全部的功能这种情况）
    // 于是把"变化的部分"独立出去，鸭子类更易于拓展，否则可能需要处理很多无用的覆写啦😄
    // 实际上变化的功能，交给具体的实现去做啦
    /*
     * PS: 让鸭子实现直接实现FlyBehavior接口的话，也相当于只做了一半的工作。改变鸭子的行为，
     *     依然需要改变实现，这就是所谓"面对实现编程"
     */
    public void performFly() {
        flyBehavior.fly();
    }

    public void performQuark() {
        quarkBehavior.quark();
    }

    // 通过使用策略模式，不局限于规范行为的接口，可以动态改变实现的行为
    public void setFlyBehavior(FlyBehavior fb) {
        this.flyBehavior = fb;
    }

    public void setQuarkBehavior(QuarkBehavior qb) {
        this.quarkBehavior = qb;
    }
}
