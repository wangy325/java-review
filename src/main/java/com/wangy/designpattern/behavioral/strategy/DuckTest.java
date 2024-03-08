package com.wangy.designpattern.behavioral.strategy;


/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:57
 */
public class DuckTest {

    public static void main(String[] args) {
        MallardDuck mock = new MallardDuck();
        mock.performFly();
        mock.performQuark();
        // 改变行为试试
        mock.setFlyBehavior(new FlyWithRocket());
        mock.performFly();
    }
}
