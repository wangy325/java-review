package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 13:03
 * // 饮料
 */
public abstract class Beverage {
    String description = "Unknown Beverage";

    Size size = Size.MEDIUM;

    double price = 0.0d;

    public String getDescription() {
        return description;
    }

    public abstract double cost();

}
