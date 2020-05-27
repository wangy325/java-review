package com.wangy.review.classandinterface;

/**
 * class should implement all abstract method
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/27 / 14:35
 */
public class InterfaceTest3 {

    private class Cell implements Cellphone {

        @Override
        public void send(String msg) {
            System.out.println("Cell.send() " + msg);
        }

        @Override
        public void acceptPhone() {
            System.out.println("Cell.acceptPhone()");
        }
    }

    private class MiPhone extends Cell implements Iphone {

        @Override
        public void play() {

        }

        @Override
        public void navigate() {

        }

    }


}

interface Cellphone {
    void send(String msg);

    void acceptPhone();
}

interface Iphone extends Cellphone {

    @Override
    void send(String msg);

    @Override
    void acceptPhone();

    void play();

    void navigate();
}
