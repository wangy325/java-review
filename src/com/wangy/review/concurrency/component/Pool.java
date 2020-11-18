package com.wangy.review.concurrency.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/18 / 22:08
 */
class Pool<T> {
    private final int size;
    final List<T> items = new ArrayList<>();
    private final boolean[] checkedOut;
    private final Semaphore available;

    public Pool(Class<T> classObject, int size) {
        this.size = size;
        checkedOut = new boolean[size];
        // 一般将公平性参数设置为true
        available = new Semaphore(size, true);
        // Load pool with objects that can be checked out:
        for (int i = 0; i < size; ++i) {
            try {
                // Assumes a default constructor:
                items.add(classObject.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    T checkOut() throws InterruptedException {
        available.acquire();
        return getItem();
    }

    void checkIn(T x) {
        if (releaseItem(x)) {
            available.release();
            System.out.println("release " + x);
        }
    }

    void checkAllIn() {
        available.release(releaseAll());
    }

    private synchronized T getItem() {
        for (int i = 0; i < size; ++i) {
            if (!checkedOut[i]) {
                checkedOut[i] = true;
                return items.get(i);
            }
        }
        // Semaphore prevents reaching here
        // 有许可一定有对象可用
        return null;
    }

    private synchronized boolean releaseItem(T item) {
        int index = items.indexOf(item);
        if (index == -1) {
            return false; // Not in the list
        }
        if (checkedOut[index]) {
            checkedOut[index] = false;

            return true;
        }
        // Wasn't checked out
        return false;
    }

    private synchronized int releaseAll() {
        int r = 0;
        for (int i = 0; i < items.size(); i++) {
            if (checkedOut[i]) {
                checkedOut[i] = false;
                ++r;
            }
        }
        return r;
    }
}
