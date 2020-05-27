package com.wangy.review.classandinterface;

/**
 * simple factory example
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/20 / 11:46
 */
public class SimpleFactory {

    public void serviceConsumer(ServiceFactory sf) {
        Service s = sf.getService();
        s.service_a();
        s.service_b();
    }

    public static void main(String[] args) {
        SimpleFactory sf = new SimpleFactory();
        sf.serviceConsumer(new NameServiceFactory());
        sf.serviceConsumer(new AgeServiceFactory());
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
    NameService() {}

    @Override
    public void service_a() {System.out.println("NameService.service_a()");}

    @Override
    public void service_b() {System.out.println("NameService.service_b()");}
}

class NameServiceFactory implements ServiceFactory {
    @Override
    public Service getService() {return new NameService();}
}

class AgeService implements Service {
    AgeService() {
    }

    @Override
    public void service_a() {System.out.println("AgeService.service_a()");}

    @Override
    public void service_b() {System.out.println("AgeService.service_b()");}
}

class AgeServiceFactory implements ServiceFactory {

    @Override
    public Service getService() {return new AgeService();}
}
