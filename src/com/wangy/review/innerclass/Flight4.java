package com.wangy.review.innerclass;

/**
 * inner class in a statement scope
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/19 / 14:44
 */
class FlightShip {
    Dest flight(boolean fly) {

        if (fly) {
            // inner class scope
            class InternalFlight implements Dest {
                private String to;

                InternalFlight(String to) {
                    this.to = to;
                }

                @Override
                public String showDest() {
                    return to;
                }
            }
            InternalFlight f = new InternalFlight("TaiPei");
            System.out.println(f);
            System.out.println(f.showDest());
            return f;
        }
        return null;
        // illegal access!
        // InternalFlight f = new InternalFlight("TaiPei");
    }
}

public class Flight4 {
    public static void main(String[] args) {
        FlightShip f = new FlightShip();
        Dest dest = f.flight(true);
        System.out.println(dest.showDest());
    }
}
