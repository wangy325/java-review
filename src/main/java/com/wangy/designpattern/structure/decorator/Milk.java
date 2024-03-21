package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 13:26
 * 牛奶
 */
public class Milk extends Condiment {

    public Milk(Beverage beverage) {
        this.beverage = beverage;
        this.size = beverage.size;
        switch (beverage.size) {
            case MEDIUM:
                price = 0.99;
                break;
            case LARGE:
                price = 1.49;
                break;
            case EXTRA_LARGE:
                price = 1.99;
                break;
            default:
        }
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + " Milk";
    }

    @Override
    public double cost() {
        //
        return beverage.cost() + price;
    }
}
