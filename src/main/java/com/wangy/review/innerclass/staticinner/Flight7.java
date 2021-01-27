package com.wangy.review.innerclass.staticinner;

/**
 * static inner class example
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/20 / 18:55
 */
public class Flight7 {
    public static void main(String[] args) {
        // out of scope! private inner class must upcast
        // StaticFlight.InnerComp comp = StaticFlight.comp();
        Comp comp = StaticFlight.comp();
        StaticFlight.InnerDest dest = StaticFlight.dest("GZ");
        // cannot access!
        // StaticFlight.InnerComp.g();
        System.out.println("StaticFlight.InnerDest.x = " + StaticFlight.InnerDest.x);
        System.out.println(comp.showComp());
        System.out.println(dest.showDest());
        StaticFlight.InnerDest.AnotherLevel.f();
        StaticFlight.InnerDest.AnotherLevel l = StaticFlight.InnerDest.anotherLevel();
        System.out.println("StaticFlight.InnerDest.AnotherLevel.p(): "+ l.p());
    }
}

interface Dest {
    String showDest();
}

interface Comp{
    String showComp();
}

class StaticFlight{

    private int constant = 10;
    private static int constant_b = 10;

     private static class InnerComp implements Comp {
        private String comp = "AIR CHINA";
        // cannot access non-static filed of outer class
        // private int a = constant;
        @Override
        public String showComp() { return comp; }
        static void g(){ System.out.println("g()"); }
    }

    static class InnerDest implements Dest{
        private String to;
        private InnerDest(String to) { this.to = to; }

        @Override
        public String showDest() { return to; }
        // static elements
        static int x =  constant_b;
        static class AnotherLevel{
            static void f(){ System.out.println("StaticFlight.InnerDest.AnotherLevel.x = " + y); }
            int p(){ return ++x; }
            static int y = constant_b;
            // same as
            // static int y = x
        }

        static AnotherLevel anotherLevel(){ return new AnotherLevel(); }

    }

    static InnerComp comp(){ return new InnerComp(); }

    static InnerDest dest(String s){ return new InnerDest(s); }
}
