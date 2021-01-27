package com.wangy.review.lambda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/17 / 16:19
 */
public class FreeVariable {

    public static void main(String[] args) {
        repeatMessage("Hello World", 1000);
    }

    static void repeatMessage(String msg, int delay) {
        ActionListener listener = e -> {
            System.out.println(msg);
            Toolkit.getDefaultToolkit().beep();
        };
        new Timer(delay, listener).start();
    }
}

