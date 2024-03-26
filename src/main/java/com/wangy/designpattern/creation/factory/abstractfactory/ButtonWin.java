package com.wangy.designpattern.creation.factory.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:39
 */
public class ButtonWin implements Button{
    @Override
    public void paint() {
        System.out.println("win: button click");
    }
}
