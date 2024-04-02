package com.wangy.designpattern.behavioral.template_method;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/2 / 11:13
 */
public class Coffee extends AbstractCaffeineBeverage {
    // 来冲一杯咖啡吧
    @Override
    public void raw() {
        System.out.println("Brew coffee Grinds and add it.");
    }

    @Override
    public void condiment() {
        System.out.println("Add some milk and sugar.");
    }

}
