package com.wangy.designpattern.behavioral.observer;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 17:39
 */
public class WeatherStationClient {
    public static void main(String[] args) {
        WeatherStation client = new WeatherStation();
        NormalBoard normalBoard =  new NormalBoard();

        client.registerBoard(normalBoard);
        // client.setData(23.2f, 10.91f, 1.01f);

        // register a new listener
        StatisticsBoard statisticsBoard =  new StatisticsBoard();
        client.registerBoard(statisticsBoard);
        client.setData(23.2f, 10.91f, 1.01f);

        client.unregisterBoard(normalBoard);
        client.setStatus(true);
        client.notifyBoard();
    }
}
