package com.wangy.designpattern.behavioral.command.rec;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 10:45
 */
public class DV {

    private void setDV(String dv){
        System.out.println("Ready to play " + dv + ".");
    }

    private void on(){
        System.out.println("DV on");
    }

    public void off(){
        System.out.println("DV off.");
    }

    public void play(String dv){
        on();
        setDV(dv);
    }
}
