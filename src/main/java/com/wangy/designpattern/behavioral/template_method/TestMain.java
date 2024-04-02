package com.wangy.designpattern.behavioral.template_method;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/2 / 11:46
 */
public class TestMain {
    public static void main(String[] args) {
//        test();
        testHook();
    }

    static void test() {
        Coffee coffee = new Coffee();
        coffee.prepareRecipe();
        System.out.println("---------------");
        Tea tea = new Tea();
        tea.prepareRecipe();
    }

    static void testHook() {
        CoffeeWithHook coffeeWithHook = new CoffeeWithHook();
        coffeeWithHook.prepareRecipe();
    }
}
