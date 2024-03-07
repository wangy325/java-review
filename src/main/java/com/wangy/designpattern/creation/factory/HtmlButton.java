package com.wangy.designpattern.creation.factory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 17:30
 */
public class HtmlButton implements Button{
    @Override
    public void render() {
        System.out.println("=====> render Web Button.");
    }

    @Override
    public void onClick() {
        System.out.println("====> click Html button.");
    }
}
