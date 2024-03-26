package com.wangy.designpattern.creation.factory.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:40
 */
public class GUIFactoryMac implements GUIFactory{
    @Override
    public Button createButton() {
        return new ButtonMac();
    }

    @Override
    public CheckBox createCheckBox() {
        return new CheckBoxMac();
    }
}
