package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 14:54
 * 豆浆
 */
public class Soy extends Condiment {

    public Soy(Beverage beverage) {
        this.beverage = beverage;
        this.size = beverage.size;
        switch (beverage.size) {
            case MEDIUM:
                price = 0.49;
                break;
            case LARGE:
                price = 0.99;
                break;
            case EXTRA_LARGE:
                price = 1.29;
                break;
            default:
        }
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + " Soy";
    }

    @Override
    public double cost() {
        return beverage.cost() + price;
    }
}
