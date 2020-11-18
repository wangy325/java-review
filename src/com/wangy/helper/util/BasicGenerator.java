package com.wangy.helper.util;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/18 / 23:39
 */
public class BasicGenerator<T> implements Generator<T> {
    private Class<T> type;

    public BasicGenerator(Class<T> type) {
        this.type = type;
    }

    @Override
    public T next() {
        try {
            // Assumes type is a public class:
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Produce a Default generator given a type token: */
    public static <T> Generator<T> create(Class<T> type) {
        return new BasicGenerator<>(type);
    }
}
