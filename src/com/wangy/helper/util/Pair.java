package com.wangy.helper.util;

/**
 * @author mindview
 * @version 1.0
 * @date 2020/11/22 / 23:13
 */
public class Pair<K,V> {
    public final K key;
    public final V value;

    public Pair(K k, V v) {
        this.key = k;
        this.value = v;
    }
}
