package com.wangy.designpattern.creation.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:48
 */
public class Main {
    static GUIFactory factory;
    static Button button;
    static CheckBox checkBox;

    public static void main(String[] args) {
        String os = "mac";
        if (args.length != 0) os = args[0];

        init(os);

        button = factory.createButton();
        checkBox = factory.createCheckBox();

        button.paint();
        checkBox.paint();

    }

    public static void init(String os) {
        System.setProperty("current.os", os);

        if (System.getProperty("current.os").equals("windows"))
            factory = new WinFactory();
        else factory = new MacFactory();
    }
}
