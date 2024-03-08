package com.wangy.designpattern.behavioral.observer;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 16:59
 */
public interface Subject {

    void registerBoard(Board board);

    void unregisterBoard(Board board);

    void notifyBoard();


    // other businesses
}
