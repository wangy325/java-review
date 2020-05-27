package com.wangy.review.innerclass;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/18 / 17:06
 */

interface Selector{
    boolean end();
    Object current();
    void next();
}

class Sequence1 {
    private Object[] items;
    private int next = 0;

    public Sequence1(int size) {
        items  = new Object[size];
    }
    public void add(Object o){
        if (next < items.length){
            items[next++] = o;
        }
    }
    private class SequenceSelector implements Selector{
        private int i = 0;

        @Override
        public boolean end() { return  i == items.length; }

        @Override
        public Object current() { return items[i]; }

        @Override
        public void next() { if (i < items.length)  i++;  }
    }

    public SequenceSelector selector(){ return new SequenceSelector(); }


}

public class Sequence{
    public static void main(String[] args) {
        Sequence1 s = new Sequence1(10);
        for (int i = 0; i <10 ; i++) {
            s.add(Integer.toString(i));
        }
        Selector selector = s.selector();
        while (!selector.end()){
            System.out.println(selector.current());
            selector.next();
        }
    }
}


