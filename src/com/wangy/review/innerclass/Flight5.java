package com.wangy.review.innerclass;


/**
 * anonymous inner class implements interface
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/19 / 15:36
 */
public class Flight5 {
    public static void main(String[] args) {
        Flight5 f5 = new Flight5();
        AirInfo airInfo = f5.airInfo("BY2020", "HK", 3.5);
        AnonFlight af = new AnonFlight();
        Dest hk = af.flight(airInfo);
        System.out.println(hk.showDest());
    }

    AirInfo airInfo(String no, String dest, Double cost){
        return new AirInfo(no, dest,cost);
    }
}

class AnonFlight {
    Dest flight(AirInfo info) {
        return new Dest() {
            private String to = info.getDest();

            @Override
            public String showDest() {
                // info = new AirInfo("JJ","JJ",1.0);   // not allowed!
                return to;
            }
        };
    }
}

class AirInfo{
    private String no;
    private String dest;
    private double timeCost;

    public AirInfo(String no, String dest, double timeCost) {
        this.no = no;
        this.dest = dest;
        this.timeCost = timeCost;
    }

    public String getDest() {
        return dest;
    }
}
