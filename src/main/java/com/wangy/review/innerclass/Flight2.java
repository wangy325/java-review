package com.wangy.review.innerclass;


/**
 *  general inner class example 2
 * @author wangy
 * @version 1.0
 * @date 2020/4/18 / 14:52
 */
public class Flight2 {
    class Comp{
        private String name;
        public String getName(){
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    class Dest{
        private String to;
        public Dest(String to) {
            this.to = to;
        }
        public String showDest(){
            return to;
        }
    }

    public Comp comp(){
        return new Comp();
    }

    public Dest dest(String to){
        return new Dest(to);
    }

    public void ship(){
        Comp c = comp();
        Dest d = dest("HK");
        System.out.println("the flight is " + c.getName() + " to " + d.showDest());
    }

    public static void main(String[] args) {
        Flight2 f2 = new Flight2();
        f2.ship();

        Flight2 f2s =  new Flight2();
        Flight2.Comp comp = f2s.comp();
        Flight2.Dest d2 = f2s.dest("New York");
    }
}
