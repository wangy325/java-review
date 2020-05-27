package com.wangy.review.innerclass.staticinner;

/**
 * static inner class in interface domain
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/21 / 01:25
 */
public interface InnerclassInterface {

    void m();

    class Test implements InnerclassInterface{

        @Override
        public void m() {
            System.out.println("man!");
        }
    }

    static Test test(){
        return new Test();
    }

    class Main{
        public static void main(String[] args) {
            Test test = test();
            test.m();
        }
    }
}
