package com.wangy.designpattern.behavioral.command;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 10:41
 */
public class RemoteControl {

    private Command command;

    public RemoteControl(Command command) {
        this.command = command;
    }

    public void onButtonPress(){
        command.execute();
    }

    public void redoCommand(){
        command.undo();
    }
}
