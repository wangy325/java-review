package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 15:05
 * 浓缩咖啡
 */
public class Espresso extends Beverage {

    public Espresso() {
        this.price = 2.99;
        this.description = "Espresso";
    }

    public Espresso(Size size) {
        this.size = size;
        this.description = "Espresso";
        switch (size) {
            case MEDIUM:
                this.price = 2.99;
                break;
            case LARGE:
                this.price = 3.99;
                break;
            case EXTRA_LARGE:
                this.price = 4.99;
                break;
            default:
        }
    }

    @Override
    public String getDescription() {
        return this.size.name() + " " + this.description;
    }

    @Override
    public double cost() {
        return price;
    }
}
