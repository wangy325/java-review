package com.wangy.designpattern.behavioral.observer;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 17:24
 */
public class NormalBoard implements Board {

    @Override
    public void update(WeatherStation client) {
        System.out.printf("Today's weather:" +
            "\n Temperature %.2f celsius" +
            "\n Humidity %.2f" +
            "\n Pressure %.2f\n",
            client.getTemperature(),
            client.getHumidity(),
            client.getPressure());
    }
}
