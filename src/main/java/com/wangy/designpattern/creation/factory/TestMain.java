package com.wangy.designpattern.creation.factory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 17:35
 */
public class TestMain {

    public static void main(String[] args) {
        String os = "windows";
        if (args != null && args.length != 0) os = args[0];

        Dialog dialog = init(os);

        dialog.render();

    }

    private static Dialog init( String os) {
        System.setProperty("current.OS", os);
        Dialog dialog;
        if (System.getProperty("current.OS").equals("web")){
            dialog = new DialogHTML();
        }else{
            dialog = new DialogWin();
        }
        return dialog;
    }
}
