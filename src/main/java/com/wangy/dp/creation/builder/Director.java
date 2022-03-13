package com.wangy.dp.creation.builder;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 23:00
 */
public class Director {

    private  Builder builder;

    public Builder getBuilder() {
        return builder;
    }

    void createSportCar(Builder builder){
        builder.reset();
        builder.setSeats(2);
        builder.setEngine(new SportEngine());
        builder.setTripComputer(true);
        builder.setGPS(true);
    }


    void createSUV(Builder builder){
        builder.reset();
        builder.setSeats(7);
        builder.setEngine(new NormalEngine());
        builder.setTripComputer(true);
        builder.setGPS(true);
    }
}
