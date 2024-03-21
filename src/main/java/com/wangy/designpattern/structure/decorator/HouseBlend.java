package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 14:11
 * 混合咖啡
 */
public class HouseBlend extends Beverage {

    public HouseBlend(Size size) {
        this.size = size;
        this.description = "House-Blend";
        switch (size){
            case MEDIUM: this.price = 4.99; break;
            case LARGE: this.price = 5.99; break;
            case EXTRA_LARGE: this.price = 6.99; break;
            default:
        }
    }

    public HouseBlend() {
        this.price = 4.99;
        this.description = "House-Blend";
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
