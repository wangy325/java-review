package com.wangy.designpattern.behavioral.strategy;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:50
 */
public class MallardDuck extends Duck {

    public MallardDuck() {
        this.quarkBehavior = new Quark();
        this.flyBehavior = new FlyWithWings();
    }

    @Override
    public void swim() {
        //...
    }

    @Override
    public void display() {
        //...
    }

}
