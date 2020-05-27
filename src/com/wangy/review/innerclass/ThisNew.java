package com.wangy.review.innerclass;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/18 / 17:34
 */
public class ThisNew {

    public static void main(String[] args) {
        DotThis d = new DotThis();
        DotThis.Inner ti = d.inner();
        ti.outer().f();

        DotNew n = new DotNew();
        DotNew.Inner ni = n.new Inner();
        ni.f();
    }
}

class DotThis{
    void f(){
        System.out.println("DotThis.f()");
    }
    class Inner{
        public DotThis outer(){
            return DotThis.this;
        }
    }

    Inner inner(){ return new Inner(); }
}

class DotNew{
    class Inner{
        void f(){ System.out.println("DotNew.Inner.f()"); }
    }
}
