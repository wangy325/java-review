package com.wangy.designpattern.structure.adaptor;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/1 / 17:17
 */

/**
 * 想要调用Iterator的接口，
 * 但是Enumeration的实现调用不了Iterator的方法，
 * 于是做了一个适配器。
 * ！Java9 已经做好了哈哈
 */
public class EnumerationIteratorAdaptor<E> implements Enumeration<E> {

    Iterator<E> iterator;

    public EnumerationIteratorAdaptor(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    @Override
    public E nextElement() {
        return iterator.next();
    }
}


