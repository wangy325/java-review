package com.wangy.review.container.map;

import java.util.Map;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/9/26 / 06:14
 */
public class NodeTest<K, V> {

    final Node<K, V>[] table = new Node[4];
    final Node<K, V>[] newtab = new Node[8];

    // 构造代码块，构造NodeTest实例时执行
    {
        Node node = new Node(5, "five", null);
        Node node1 = new Node(3, "four", null);
        Node node2 = new Node(7, "three", node1);
        Node node3 = new Node(11, "two", node2);
        Node node4 = new Node(15, "one", node3);
        Node node5 = new Node(17, "six", node4);
        Node node6 = new Node(21, "seven", node5);
        Node node7 = new Node(22, "eight", null);
        Node node8 = new Node(23, "nine", null);

        table[0] = node;
        table[1] = node7;
        table[2] = node6;
        table[3] = node8;
    }

    public static void main(String[] args) {

        NodeTest<Integer, String> nt = new NodeTest<>();

        // 实际上可以直接复制链表，其next域可以复制过来
//        nt.copyAndPrintTable(table, newtab);

        // 看看HashMap源码的resize方法的复制部分究竟搞什么飞机
        nt.resize(nt.table, nt.newtab);
        // 看看此时的newtab
        nt.printTable(nt.newtab);

        // 简单的实验
//        System.out.println("-------");
//            node.nodeSpilt(node6);
    }

    public void copyAndPrintTable(Node<K, V>[] table, Node<K, V>[] newtab) {
        Node<K, V> f, n;
        for (int i = 0; i < table.length; i++) {
            newtab[i] = table[i];
            if ((f = newtab[i]).next == null) {
                System.out.println(f.getKey() + ", " + f.getValue());
            } else {
                do {
                    n = f.next;
                    System.out.println(f.getKey() + ", " + f.getValue());
                } while ((f = n) != null);
            }
        }
    }

    public void printTable(Node<K, V>[] newtab) {
        Node<K, V> g, h;
        for (int i = 0; i < newtab.length; i++) {
            if ((g = newtab[i]) != null) {
                if (g.next == null) {
                    System.out.println("newtab[" + i + "]" + g.getKey() + ", " + g.getValue());
                } else {
                    do {
                        h = g.next;
                        System.out.println("newtab[" + i + "]" + g.getKey() + ", " + g.getValue());
                    } while ((g = h) != null);
                }
            }
        }
    }

    public void resize(Node<K, V>[] table, Node<K, V>[] newtab) {
        int oldcap = table.length;
        for (int j = 0; j < oldcap; ++j) {
            Node<K, V> e;
            if ((e = table[j]) != null) {
                table[j] = null;
                if (e.next == null) {
                    newtab[j] = e;
                } else { // preserve order
                    Node<K, V> loHead = null, loTail = null;
                    Node<K, V> hiHead = null, hiTail = null;
                    Node<K, V> next;
                    do {
                        next = e.next;
                        if ((e.key.hashCode() & oldcap) == 0) {
                            /*printNode(e);
                            System.out.println("^^^^^^");
                            printNode(loHead);
                            System.out.println("----");
                            printNode(loTail);
                            System.out.println("--------------");*/
                            if (loTail == null) {
                                loHead = e;
                            } else {
                                loTail.next = e;
                            }
                            loTail = e;

                            /*System.out.println("---------------after-----------");
                            printNode(e);
                            System.out.println("^^^^^^");
                            printNode(loHead);
                            System.out.println("----");
                            printNode(loTail);
                            System.out.println("--------------");*/
                        } else {
                            if (hiTail == null) {
                                hiHead = e;
                            } else {
                                hiTail.next = e;
                            }
                            hiTail = e;
                        }
                    } while ((e = next) != null);

                    if (loTail != null) {
                        loTail.next = null;
                        newtab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newtab[j + oldcap] = hiHead;
                    }
                }
            }
        }
    }


    public void printNode(Node<K, V> e) {
        Node<K, V> c, d;
        if ((c = e) == null) return;
        do {
            d = c.next;
            System.out.println(c.key + ", " + c.value);
        } while ((c = d) != null);
    }

    public void nodeSpilt(Node<K, V> node) {
        // 做个简单的实验
        // s和p均为指向node的引用，对s的修改也会影响到p
        // 引用可变
        Node<K, V> s = node, p = node;
        System.out.println(s.next.getKey());

        Node<K, V> next;
        next = node.next;
        if (next != null) {
            p.next = null;
        }
        System.out.println(s.next == null); //true
    }

    static class Node<K, V> implements Map.Entry<K, V> {

        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override

        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        @Override
        public V setValue(V value) {
            return null;
        }
    }
}
