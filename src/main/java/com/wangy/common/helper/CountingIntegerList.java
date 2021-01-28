//: net/mindview/util/CountingIntegerList.java
// List of any length, containing sample data.
package com.wangy.common.helper;

import java.util.AbstractCollection;
import java.util.AbstractList;


/**
 * 这个类构建一个<b>不可修改</b>的"random access list"，构造器指定了list的size，但没有向list中添加任何元素
 * <p>
 * 那么元素是如何添加进去的呢，这是一个有趣的问题
 * <p>
 * 由于是不可修改的list，最简单的需求就是只实现{@link AbstractList#get(int)}  get()}和
 * {@link AbstractCollection#size() size()}方法，那么只有get()方法可以"被动"的返回索引
 * 位置的值，我们找找看get()方法是在什么时候被"偷偷"调用的
 * <p>
 * 在main()方法中，我们打印了一个带有初始容量的类实例，很容易就能知道，实际上是调用了该类的基类
 * AbstractCollection的{@link AbstractCollection#toString() toString()}方法：
 *
 * <pre>
 *     public String toString() {
 *         Iterator<E> it = iterator();
 *         if (! it.hasNext())
 *             return "[]";
 *
 *         StringBuilder sb = new StringBuilder();
 *         sb.append('[');
 *         for (;;) {
 *             <b>E e = it.next();</b>
 *             sb.append(e == this ? "(this Collection)" : e);
 *             if (! it.hasNext())
 *                 return sb.append(']').toString();
 *             sb.append(',').append(' ');
 *         }
 *     }
 * </pre>
 * <p>
 * 可以看到，<code>toString()</code>通过迭代器遍历了list中的元素，迭代器的<code>next()</code>
 * 方法实际调用了{@link AbstractList.Itr#next() next()}方法：
 *
 * <pre>
 *      public E next() {
 *             checkForComodification();
 *             try {
 *                 int i = cursor;
 *                 <b>E next = get(i);</b>
 *                 lastRet = i;
 *                 cursor = i + 1;
 *                 return next;
 *             } catch (IndexOutOfBoundsException e) {
 *                 checkForComodification();
 *                 throw new NoSuchElementException();
 *             }
 *         }
 * </pre>
 * <p>
 * 哈，找到了！迭代器在"初始化"list的元素
 * <p>
 * 实际上，这个list的值是由{@link CountingIntegerList#get(int)}方法指定的，当调用时即会获得一个值
 *
 * @author mindview
 */
public class CountingIntegerList extends AbstractList<Integer> {
    private int size;

    public CountingIntegerList(int size) {
        this.size = Math.max(size, 0);
    }

    @Override
    public Integer get(int index) {
        return Integer.valueOf(index);
    }

    @Override
    public int size() {
        return size;
    }

    public static void main(String[] args) {
        System.out.println(new CountingIntegerList(30));
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
*///:~