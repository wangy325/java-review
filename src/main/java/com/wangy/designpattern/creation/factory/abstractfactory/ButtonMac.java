package com.wangy.designpattern.creation.factory.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:40
 */
public class ButtonMac implements Button{
    @Override
    public void paint() {
        System.out.println("mac: button click");
    }
}
