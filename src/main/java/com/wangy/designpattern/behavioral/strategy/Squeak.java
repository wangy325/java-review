package com.wangy.designpattern.behavioral.strategy;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:41
 */
public class Squeak implements QuarkBehavior{
    @Override
    public void quark() {
        System.out.println("Squeak!");
    }
}
