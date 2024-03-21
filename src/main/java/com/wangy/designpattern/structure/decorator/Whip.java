package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 14:58
 * 奶泡
 */
public class Whip extends Condiment{

    public Whip(Beverage beverage) {
        this.beverage = beverage;
        this.size = beverage.size;
        switch (beverage.size) {
            case MEDIUM:
                price = 1.49;
                break;
            case LARGE:
                price = 1.99;
                break;
            case EXTRA_LARGE:
                price = 2.49;
                break;
            default:
        }
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + " Whip";
    }

    @Override
    public double cost() {
        return beverage.cost() + price;
    }
}
