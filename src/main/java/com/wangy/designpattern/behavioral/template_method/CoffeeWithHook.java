package com.wangy.designpattern.behavioral.template_method;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/2 / 12:14
 */
public class CoffeeWithHook extends AbsCaffeineBeverageWithHook {
    @Override
    public void raw() {
        System.out.println("Brew coffee Grinds and add it.");
    }

    @Override
    public void condiment() {
        if (addCondiment()) {
            System.out.println("Add some milk and sugar.");
        }
    }

    @Override
    public boolean addCondiment() {
        return condimentOrNot().equals("y");
    }

    private String condimentOrNot() {
        String str;
        System.out.println("Would you like to add some milk and sugar(y/n)?");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            str = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str == null ? "n" : str;
    }
}
