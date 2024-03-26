package com.wangy.designpattern.creation.factory.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:37
 * 抽象工厂，可以创建多种产品
 */
public interface GUIFactory {

    Button createButton();

    CheckBox createCheckBox();
}
