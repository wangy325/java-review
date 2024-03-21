package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 15:00
 * 深烘焙
 */
public class DarkRost extends Beverage {

    public DarkRost() {
        this.price = 3.99;
        this.description = "DarkRoast";
    }

    public DarkRost(Size size){
        this.size = size;
        this.description = "DarkRoast";
        switch (size){
            case MEDIUM: this.price = 3.99; break;
            case LARGE: this.price = 4.99; break;
            case EXTRA_LARGE: this.price = 5.99; break;
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
