package com.wangy.designpattern.creation.factory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 17:26
 */
public abstract class Dialog {

    abstract Button createButton();

    /**
     *  请注意，创建者的主要职责并非是创建产品。其中通常会包含一些核心业务
     *  逻辑，这些逻辑依赖于由工厂方法返回的产品对象。子类可通过重写工厂方
     *  法并使其返回不同类型的产品来间接修改业务逻辑。
     */
    void render(){
        Button button = createButton();
        button.onClick();
        button.render();
    }
}
