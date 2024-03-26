package com.wangy.designpattern.creation.factory.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:39
 */
public class GUIFactoryWin implements GUIFactory{
    @Override
    public Button createButton() {
        return new ButtonWin();
    }

    @Override
    public CheckBox createCheckBox() {
        return new CheckBoxWin();
    }
}
