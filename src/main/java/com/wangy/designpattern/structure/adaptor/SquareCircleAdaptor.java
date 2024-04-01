package com.wangy.designpattern.structure.adaptor;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/1 / 17:14
 */
public class SquareCircleAdaptor implements Square{

    private Circle circle;

    public SquareCircleAdaptor(Circle circle) {
        this.circle = circle;
    }

    @Override
    public void paint() {
        circle.func();
    }
}
