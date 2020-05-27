package com.wangy.review.classandinterface;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/16 / 16:43
 */
public class InterfaceTest2 {
    public static void main(String[] args) {
        W w =  new W();
        w.pType();
    }
}

interface Vita{
    default void pType(){
        System.out.println("Vita 柠檬茶");
    }
}

 abstract class Drink{
    public void pType(){
        System.out.println("Vita 奶");
    }
}

class W extends Drink implements Vita{
//    @Override
//    public void pType() {
////        super.pType();
////        Vita.super.pType();
//    }
}