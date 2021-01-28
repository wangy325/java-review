package com.wangy.review.concurrency.component;

import com.wangy.common.helper.Generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Queue;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/19 / 17:53
 */
@SuppressWarnings("all")
public class DataBuffer<T> {

    private Queue<T> buffer;
    /** 利用size构造一个有界队列 */
    private final int size;

    public DataBuffer(Class<? extends Queue<T>> cls, int size) throws Exception {
        this(cls, size, null);
    }

    public DataBuffer(Class<? extends Queue<T>> cls, int size, Generator<T> gen) throws Exception {
        if (cls == null) throw new NullPointerException();
        // 检查cls的类型，如果不是队列，则抛出异常
        if (!Queue.class.isAssignableFrom(cls)) throw new ClassCastException();
        if (size < 0) throw new IllegalArgumentException();
        this.size = size;
        try {
            Constructor<? extends Queue<T>> c = cls.getConstructor(int.class);
            c.setAccessible(true);
            this.buffer = c.newInstance(size);
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
            this.buffer = cls.newInstance();
        }

        if (gen != null) {
            for (int i = 0; i < size; i++)
                buffer.offer(gen.next());
        }
    }

    synchronized boolean isFull() {
        return buffer.size() >= size;
    }

    synchronized boolean isEmpty() {
        return buffer.isEmpty();
    }

    synchronized int bufferSize() {
        return buffer.size();
    }

    synchronized public Queue<T> getBuffer() {
        return buffer;
    }

    synchronized boolean addToBuffer(T t) {
        if (!isFull()) {
            return buffer.offer(t);
        }
        return false;
    }

    synchronized T takeFromBuffer() {
        if (!isEmpty()) {
            buffer.remove();
        }
        return null;
    }
}
