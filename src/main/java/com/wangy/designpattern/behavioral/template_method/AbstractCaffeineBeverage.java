package com.wangy.designpattern.behavioral.template_method;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/2 / 11:02
 */

/** 抽象模板方法类 */
public abstract class AbstractCaffeineBeverage {

    /**
     * boilWater
     * brewCoffeeGrinds
     * stepTeaBag
     * addMilkAndSugar
     * addLemon
     * pullInCup
     * 模板方法声明为final，防止子类继承-修改算法
     */
    public final void prepareRecipe() {
        boilWater();
        raw();
        condiment();
        pullInCup();
    }

    protected final void boilWater() {
        System.out.println("Starting boiling water.");
    }

    protected final void pullInCup() {
        System.out.println("Done! pull beverage into cup.");
    }

    public abstract void raw();

    public abstract void condiment();
}
