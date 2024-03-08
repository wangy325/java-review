package com.wangy.designpattern.behavioral.strategy;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:37
 */
public class FlyWithRocket implements FlyBehavior{
    @Override
    public void fly() {
        System.out.println("Oh! I can fly with a rocket booster!");
    }
}
