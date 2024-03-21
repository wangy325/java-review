package com.wangy.designpattern.structure.decorator;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 14:04
 * 摩卡
 */
public class Mocha extends Condiment {

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
        this.size = beverage.size;
        switch (beverage.size) {
            case MEDIUM:
                price = 1.09;
                break;
            case LARGE:
                price = 1.69;
                break;
            case EXTRA_LARGE:
                price = 2.29;
                break;
            default:
        }
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + " Mocha";
    }

    @Override
    public double cost() {
        double v = beverage.cost() + price;
        return BigDecimal.valueOf(v).round(MathContext.DECIMAL32).doubleValue();
    }
}
