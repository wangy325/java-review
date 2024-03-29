package com.wangy.designpattern.behavioral.command.cmd;

import com.wangy.designpattern.behavioral.command.Command;
import com.wangy.designpattern.behavioral.command.rec.AirConditioner;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 10:57
 */
public class AcOnCommand implements Command {
    private AirConditioner ac;

    public AcOnCommand(AirConditioner ac) {
        this.ac = ac;
    }

    @Override
    public void execute() {
        ac.on();
    }

    @Override
    public void undo() {
        ac.off();
    }
}
