package com.wangy.review.innerclass;

/**
 * general inner class example 2
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/18 / 14:36
 */
public class Flight {
    class Comp {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class Dest {
        private String to;

        public Dest(String to) {
            this.to = to;
        }

        public String showDest() {
            return to;
        }
    }

    public void flight() {
        Comp c = new Comp();
        c.setName("China Air");
        Dest d = new Dest("Los Angles");
        System.out.println("the flight is " + c.getName() + " to " + d.showDest());
    }

    public static void main(String[] args) {
        Flight f = new Flight();
        f.flight();
    }
}
