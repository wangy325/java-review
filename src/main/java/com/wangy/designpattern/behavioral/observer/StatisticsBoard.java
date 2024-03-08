package com.wangy.designpattern.behavioral.observer;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 17:53
 */
public class StatisticsBoard implements Board {
    @Override
    public void update(WeatherStation client) {
        System.out.printf("Average weather of this month:" +
                "\n Average Temperature %.2f celsius" +
                "\n Average Humidity %.2f" +
                "\n Average Pressure %.2f\n",
            client.getTemperature(),
            client.getHumidity(),
            client.getPressure());
    }
}
