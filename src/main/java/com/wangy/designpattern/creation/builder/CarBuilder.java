package com.wangy.designpattern.creation.builder;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 22:00
 */
public class CarBuilder implements Builder {

    private Car car;
    private Product product;

    public CarBuilder() {
        this.reset();
    }

    @Override
    public void reset() {
        this.car = new Car();
    }

    @Override
    public void setSeats(int seats) {
        car.setSeat(seats);
    }

    @Override
    public void setEngine(Engine engine) {
        car.setEngine(engine);
    }

    @Override
    public void setTripComputer(boolean bool) {
        car.setTripComputer(bool);
    }

    @Override
    public void setGPS(boolean gps) {
        car.setGPS(gps);
    }

    @Override
    public Car getProduct() {
        this.product = this.car;
        this.reset();
        return (Car) product;
    }
}
