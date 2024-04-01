package com.wangy.designpattern.structure.adaptor;

import java.util.Enumeration;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/1 / 17:15
 */
public class TestMain {
    public static void main(String[] args) {
        RoundCircle rc = new RoundCircle();
        SquareCircleAdaptor adaptor = new SquareCircleAdaptor(rc);
        // square paint a round circle!
        adaptor.paint();
    }

    private class Dock<E> implements Enumeration<E> {

        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public E nextElement() {
            return null;
        }
    }
}
