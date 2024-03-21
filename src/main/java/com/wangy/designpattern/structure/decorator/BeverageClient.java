package com.wangy.designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 14:14
 */
public class BeverageClient {

    public static void main(String[] args) {
        HouseBlend hb = new HouseBlend(Size.EXTRA_LARGE);

        Milk milk = new Milk(hb);
        Mocha mocha = new Mocha(milk);
        Mocha mocha1 = new Mocha(mocha);

        System.out.println(mocha1.getDescription());
        System.out.println("cost:" + mocha1.cost());
    }
}
