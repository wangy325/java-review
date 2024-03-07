package com.wangy.designpattern.creation.builder;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 21:59
 */
public interface Builder {
    void reset();

    void setSeats(int seats);

    void setEngine(Engine engine);

    void setTripComputer(boolean computer);

    void setGPS(boolean gps);

    Product getProduct();
}
