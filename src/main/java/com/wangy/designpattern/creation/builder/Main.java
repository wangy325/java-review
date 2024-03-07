package com.wangy.designpattern.creation.builder;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 23:19
 */
public class Main {

    public static void main(String[] args) {
        Director director = new Director();
        CarBuilder builder = new CarBuilder();
        director.createSportCar(builder);

        Car sportCar = builder.getProduct();

        ManualBuilder manualBuilder  = new ManualBuilder();

        director.createSportCar(manualBuilder);

        Manual sportCarManual = manualBuilder.getProduct();

        System.out.println(sportCar);

    }
}
