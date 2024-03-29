package com.wangy.designpattern.behavioral.command.cmd;

import com.wangy.designpattern.behavioral.command.Command;
import com.wangy.designpattern.behavioral.command.rec.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 11:16
 * 一个宏命令
 */
public class FamilyCinemaOnCommand implements Command {

    private AirConditioner ac;
    private Light light;
    private DV dv;
    private Popcorn popcorn;
    private Screen screen;
    private Stereo stereo;

    public FamilyCinemaOnCommand(AirConditioner ac, Light light, DV dv, Popcorn popcorn, Screen screen, Stereo stereo) {
        this.ac = ac;
        this.light = light;
        this.dv = dv;
        this.popcorn = popcorn;
        this.screen = screen;
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        ac.on();
        popcorn.popcornOn();
        stereo.stereoOn();
        screen.screenDown();
        dv.play("Schindler's List");
        light.off();
    }

    @Override
    public void undo() {
        light.on();
        dv.off();
        screen.screenUp();
        stereo.stereoOff();
        popcorn.popcornOff();
        ac.off();
    }
}
