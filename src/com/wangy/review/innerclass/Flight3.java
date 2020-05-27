package com.wangy.review.innerclass;

/**
 * inner class in a method scope
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/19 / 13:49
 */


/** interface used in other classes*/
interface Dest {
    String showDest();
}

public class Flight3 {
    private final int C = 100;
    public Dest dest(String t) {
        class PDest implements Dest {
            private String to;
            private int y;
            private PDest(String dest) {
                this.to = dest;
                this.y = Flight3.this.C;
            }

            @Override
            public String showDest() {
                // not allowed!
//                t = t.concat("xxx");
                System.out.println(y);
                return to;
            }
        }
        return new PDest(t);
    }

    public static void main(String[] args) {
        Flight3 f = new Flight3();
        Dest d = f.dest("Macao");
        System.out.println(d.showDest());
    }
}

