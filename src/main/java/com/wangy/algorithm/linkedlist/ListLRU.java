package com.wangy.algorithm.linkedlist;

/**
 * 使用单链表实现一个LRU（Least Recent Use）缓存
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/23 / 16:11
 */
public class ListLRU {

    private static final int CAPACITY = 10;

    private static final NodeList<Integer> LIST = new NodeList<>();

    /**
     * 使用缓存
     * <p>
     * 平均时间复杂度：O(n^2)
     * </p>
     *
     * @param s element to cache
     */
    static void cache(Integer s) {
        // check whether s is cached
        int index;
        if ((index = LIST.getIndex(s)) != -1) {
            // 将元素移动到队首
            LIST.remove(index);
        } else {
            // check size
            if (LIST.size() >= CAPACITY) {
                // full, remove tail and then insert s to head
                LIST.remove(CAPACITY-1);
            }
        }
        LIST.insertHead(s);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 9 ; i++) {
            ListLRU.cache(i);
        }
        ListLRU.LIST.printAll();
        ListLRU.cache(9);
        ListLRU.LIST.printAll();
        ListLRU.cache(0);
        ListLRU.LIST.printAll();
        ListLRU.cache(99);
        ListLRU.LIST.printAll();
    }
}
