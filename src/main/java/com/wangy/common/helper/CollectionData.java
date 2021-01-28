package com.wangy.common.helper;

import java.util.ArrayList;

/**
 * fill an empty collection by {@link Generator<T>}
 *
 * @author mindview
 * @version 1.0
 * @date 2020/11/21 / 21:42
 */
public class CollectionData<T> extends ArrayList<T> {
    CollectionData(Generator<T> gen, int quantity) {
        for (int i = 0; i < quantity; i++)
            add(gen.next());
    }

    // A generic convenience method:
    public static <T> CollectionData<T> list(Generator<T> gen, int quantity) {
        return new CollectionData<>(gen, quantity);
    }
}
