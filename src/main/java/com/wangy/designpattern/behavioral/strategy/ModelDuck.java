package com.wangy.designpattern.behavioral.strategy;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 16:23
 */
public class ModelDuck extends Duck {

    public ModelDuck() {
        this.flyBehavior = new FlyWithRocket();
        this.quarkBehavior = new Mute();
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
