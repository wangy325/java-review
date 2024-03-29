package com.wangy.designpattern.behavioral.command.cmd;

import com.wangy.designpattern.behavioral.command.Command;
import com.wangy.designpattern.behavioral.command.rec.AirConditioner;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 10:58
 */
public class AcOffCommand implements Command {
    private AirConditioner ac;

    public AcOffCommand(AirConditioner ac) {
        this.ac = ac;
    }

    @Override
    public void execute() {
        ac.off();
    }

    @Override
    public void undo() {
        ac.on();
    }
}
