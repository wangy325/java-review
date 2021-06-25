package com.wangy.review.algorithm.stack;

/**
 * 使用栈实现浏览器的基本前进、后退操作
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/24 / 16:57
 */
public class SimpleBrowser {
    private final Stack x = new Stack();
    private final Stack y = new Stack();


    String visit(String link) {
        x.push(link);
        y.clear();
        return link;
    }

    String back() {
        if (x.size() == 1) {
            System.out.print("当前已在最后页面了！");
            return null;
        }
        y.push(x.pop());
        String cur = x.pop();
        x.push(cur);
        return cur;
    }

    String forward() {
        String cur = y.pop();
        if (cur == null) {
            System.out.print("当前已在最前页面了！");
            return null;
        }
        x.push(cur);
        return cur;
    }

    public static void main(String[] args) {
        SimpleBrowser browser = new SimpleBrowser();
        System.out.println("visit: " + browser.visit("www.apple.com"));
        System.out.println("visit: " + browser.visit("www.bing.com"));
        System.out.println("visit: " + browser.visit("www.google.com"));
        System.out.println("back: " + browser.back());
        System.out.println("back: " + browser.back());
        System.out.println("back: " + browser.back());
        System.out.println("back: " + browser.back());
        System.out.println("back: " + browser.back());
        System.out.println("forward: " + browser.forward());
        System.out.println("visit: " + browser.visit("www.oracle.com"));
        System.out.println("back: " + browser.back());
        System.out.println("forward: " + browser.forward());
        System.out.println("forward: " + browser.forward());
        System.out.println("forward: " + browser.forward());
        System.out.println("back: " + browser.back());
    }

    /********************* 栈的基本实现 ********************/
    class Stack {
        private String[] data;

        private int size;

        private int cap;

        public Stack() {
            this.data = new String[10];
            this.cap = 10;
            this.size = 0;
        }

        int size() {
            return size;
        }

        void push(String str) {
            if (size == cap) {
                String[] newData = new String[cap * 2];
                for (int i = 0; i < size; i++) {
                    newData[i] = data[i];
                }
                data = newData;
            }
            data[size++] = str;
        }

        String pop() {
            if (size == 0) return null;
            return data[--size];
        }

        // 清空栈
        void clear() {
            // 让GC处理出栈的元素
            while (size > 0)
                pop();
//            size = 0 ;

        }
    }
}
