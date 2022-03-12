package com.wangy.dp.creation.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:40
 */
public class MacCheckBox implements CheckBox{
    @Override
    public void paint() {
        System.out.println("mac: box check");
    }
}
