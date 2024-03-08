package com.wangy.designpattern.behavioral.strategy;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:38
 */
public class FlyNoWay implements FlyBehavior{
    @Override
    public void fly() {
        System.out.println("Oops! I can not fly!");
    }
}
