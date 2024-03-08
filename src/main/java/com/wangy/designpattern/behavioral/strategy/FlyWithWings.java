package com.wangy.designpattern.behavioral.strategy;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:36
 */
public class FlyWithWings implements FlyBehavior{

    @Override
    public void fly() {
        System.out.println("Yes! I can fly with wings!");
    }
}
