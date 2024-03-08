package com.wangy.designpattern.behavioral.observer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 17:16
 */
public class WeatherStation implements Subject {

    // 并发风险
    private List<Board> boards;
    private boolean status;

    public WeatherStation() {
        this.boards = new LinkedList<>();
        this.status = false;
    }

    @Override
    public void registerBoard(Board board) {
        if (!boards.contains(board)){
            boards.add(board);
        }
    }

    @Override
    public void unregisterBoard(Board board) {
        boards.remove(board);
    }

    @Override
    public void notifyBoard() {
        if (status){
            for (Board board : boards) {
                board.update(this);
            }
            status = false;
        }
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private float temperature;
    private float humidity;
    private float pressure;

   public void setData(float temperature, float humidity, float pressure){
       this.temperature = temperature;
       this.humidity = humidity;
       this.pressure = pressure;
       this.status = true;
       notifyBoard();
   }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }
}
