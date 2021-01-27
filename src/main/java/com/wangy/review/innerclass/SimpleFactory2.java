package com.wangy.review.innerclass;

/**
 * simple factory using anonymous inner class
 * 静态工厂方法
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/20 / 12:28
 */
public class SimpleFactory2 {

    public static void main(String[] args) {
        NameService.getFactory.getService().service_a();
        NameService.getFactory.getService().service_b();
        AgeService.getFactory.getService().service_a();
        AgeService.getFactory.getService().service_b();
    }
}

interface Service {
    void service_a();

    void service_b();
}

interface ServiceFactory {
   Service getService();
}

class NameService implements Service {
    private NameService() {}

    @Override
    public void service_a() {System.out.println("NameService.service_a()");}

    @Override
    public void service_b() {System.out.println("NameService.service_b()");}

    public static ServiceFactory getFactory = new ServiceFactory() {
        @Override
        public Service getService() {
            return new NameService();
        }
    };
    // same as lambda expression below
    // public static ServiceFactory factory = () -> new NameService();
}


class AgeService implements Service {
    private AgeService() { }

    @Override
    public void service_a() {System.out.println("AgeService.service_a()");}

    @Override
    public void service_b() {System.out.println("AgeService.service_b()");}

    public static ServiceFactory getFactory = new ServiceFactory() {
        @Override
        public Service getService() {
            return new AgeService();
        }
    };
    // same as lambda expression below
    // public static ServiceFactory factory = () -> new AgeService();
}