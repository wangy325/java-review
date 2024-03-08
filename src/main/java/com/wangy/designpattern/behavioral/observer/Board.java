package com.wangy.designpattern.behavioral.observer;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 17:11
 */
public interface Board {

    // 观察者收到通知之后的更新，方法参数可以是指定字段或者实体
    void update(WeatherStation client);
}
