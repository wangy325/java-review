package com.wangy.review.concurrency.component;

import com.wangy.helper.util.Generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/19 / 17:53
 */
public class DataBuffer<T> {

    private final Generator<T> gen;
    private Queue<T> buffer;
    private  final int size;
    private boolean empty;
    private final List<Class<?>> CLASSES = Arrays.asList(ArrayBlockingQueue.class.getClasses());


    public DataBuffer(Queue<T> buffer, Generator<T> gen, int size, boolean empty) {
        if (buffer == null) throw new NullPointerException();
        /* if (!CLASSES.contains(cls)) throw new ClassCastException();*/
        if (size < 0 ) throw new IllegalArgumentException();

        /*try {
            Constructor<T> constructor = cls.getConstructor(int.class);
            constructor.setAccessible(true);
            this.buffer = (Queue<T>) constructor.newInstance(size);
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/
        this.buffer = buffer;
        this.size = size;
        this.gen = gen;
        if (gen != null && !empty) {
            for (int i = 0; i < size; i++)
                buffer.offer(gen.next());
        }
    }


    synchronized boolean isFull() {
        return buffer.size() == size;
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
