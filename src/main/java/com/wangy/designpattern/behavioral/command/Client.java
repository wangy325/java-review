package com.wangy.designpattern.behavioral.command;

import com.wangy.designpattern.behavioral.command.cmd.AcOnCommand;
import com.wangy.designpattern.behavioral.command.cmd.FamilyCinemaOnCommand;
import com.wangy.designpattern.behavioral.command.rec.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 11:48
 */
public class Client {
    public static void main(String[] args) {
//        simpleCommand();
        batchCommand();
    }

    static void simpleCommand(){
        AcOnCommand acOnCommand = new AcOnCommand(new AirConditioner());
        RemoteControl remoteControl =  new RemoteControl(acOnCommand);
        remoteControl.onButtonPress();
    }

    static void batchCommand(){
        AirConditioner ac =  new AirConditioner();
        Light light =  new Light();
        Popcorn popcorn =  new Popcorn();
        Stereo stereo= new Stereo();
        DV dv = new DV();
        Screen screen = new Screen();
        FamilyCinemaOnCommand familyCinemaOnCommand
            = new FamilyCinemaOnCommand(ac,light, dv, popcorn, screen,stereo);

        RemoteControl remoteControl =  new RemoteControl(familyCinemaOnCommand);
        remoteControl.onButtonPress();
        System.out.println("---------------");
        remoteControl.redoCommand();
    }
}
