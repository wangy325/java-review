package com.wangy.review.innerclass.staticinner;

/**
 * static inner class in interface domain
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/21 / 01:25
 */
public interface InnerClassInterface {

    void m();

    /**
     * 此类默认为静态内部类
     */
    class Test implements InnerClassInterface {

        @Override
        public void m() {
            System.out.println("man!");
        }
    }

    /**
     * 接口中可以包含静态方法
     *
     * @return InnerClassInterface
     */
    static Test test() {
        return new Test();
    }

    class Main {
        public static void main(String[] args) {
            Test test = InnerClassInterface.test();
            test.m();
        }
    }
}
