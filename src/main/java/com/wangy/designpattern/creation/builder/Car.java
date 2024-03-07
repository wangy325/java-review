package com.wangy.designpattern.creation.builder;


/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 21:58
 */
public class Car implements Product {
    private int seat;

    private Engine engine;

    private boolean tripComputer;

    private  boolean GPS;

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setTripComputer(boolean tripComputer) {
        this.tripComputer = tripComputer;
    }

    public void setGPS(boolean GPS) {
        this.GPS = GPS;
    }

    public int getSeat() {
        return seat;
    }

    public Engine getEngine() {
        return engine;
    }

    public boolean isTripComputer() {
        return tripComputer;
    }

    public boolean isGPS() {
        return GPS;
    }

    @Override
    public String toString() {
        return "Car{" +
            "seat=" + seat +
            ", engine=" + engine +
            ", tripComputer=" + tripComputer +
            ", GPS=" + GPS +
            '}';
    }
}
