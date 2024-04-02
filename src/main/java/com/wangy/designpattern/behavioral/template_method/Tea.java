package com.wangy.designpattern.behavioral.template_method;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/2 / 11:15
 */
public class Tea extends AbstractCaffeineBeverage{
    // 茶里面的咖啡因含量是咖啡豆的3倍！
    @Override
    public void raw() {
        System.out.println("Put tea bag.");
    }

    @Override
    public void condiment() {
        System.out.println("Add some lemon juice.");
    }
}
