package com.wangy.review.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 链接：<a href="https://www.nowcoder.com/questionTerminal/42852fd7045c442192fa89404ab42e92?f=discussion">字节-自动校对程序</a>
 * 来源：牛客网
 * <p>
 * 空间限制：C/C++ 32M，其他语言64M<br>
 * 我叫王大锤，是一家出版社的编辑。我负责校对投稿来的英文稿件，这份工作非常烦人，因为每天都要去修正无数的拼写错误。但是，
 * 优秀的人总能在平凡的工作中发现真理。我发现一个发现拼写错误的捷径：
 * <p>
 * 1. 三个同样的字母连在一起，一定是拼写错误，去掉一个的就好啦：比如 helllo -> hello<br>
 * 2. 两对一样的字母（AABB型）连在一起，一定是拼写错误，去掉第二对的一个字母就好啦：比如 helloo -> hello<br>
 * 3. 上面的规则优先“从左到右”匹配，即如果是AABBCC，虽然AABB和BBCC都是错误拼写，应该优先考虑修复AABB，结果为AABCC<br>
 * <p>
 * 我特喵是个天才！我在蓝翔学过挖掘机和程序设计，按照这个原理写了一个自动校对器，工作效率从此起飞。用不了多久，我就会出任CEO，
 * 当上董事长，迎娶白富美，走上人生巅峰，想想都有点小激动呢！<br>
 * ……
 * <br>
 * 万万没想到，我被开除了，临走时老板对我说： “做人做事要兢兢业业、勤勤恳恳、本本分分，人要是行，干一行行一行。一行行行行行；
 * 要是不行，干一行不行一行，一行不行行行不行。” 我现在整个人红红火火恍恍惚惚的……
 * <p>
 * 请听题：请实现大锤的自动校对程序
 * <p>
 * 输入描述:<br>
 * 第一行包括一个数字N，表示本次用例包括多少个待校验的字符串。
 * <p>
 * 后面跟随N行，每行为一个待校验的字符串。
 * <p>
 * <p>
 * 输出描述:<br>
 * N行，每行包括一个被修复后的字符串。<br>
 * 示例1<br>
 * 输入
 * <pre>
 *     2
 *     helllo
 *     wooooooow
 * </pre>
 * 输出
 * <pre>
 *     hello
 *     woow
 * </pre>
 *
 * @author wangy
 * @version 1.0
 * @date 2020/12/1 / 18:42
 */
public class TrimRepeat {

    static List<String> list = new ArrayList<>();
    static int count;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String next = scanner.next();
        count = Integer.parseInt(next);

        for (int i = 0; i < count; i++) {
            list.add(scanner.next());
        }

        for (String s : list) {
            System.out.println(revise(s));
        }
    }

    private static String revise(String s) {
        int index = 0;
        if (s.length() < 3) return s;
        char[] prefix = new char[]{0, 0, 0};
        StringBuilder rs = new StringBuilder();
        while (index < s.length()) {
            char c = s.charAt(index++);
            if (prefix[0] == 0) {
                prefix[0] = c;
                rs.append(prefix[0]);
                continue;
            }
            if (prefix[1] == 0) {
                if (c != prefix[0]) {
                    prefix[0] = c;
                } else {
                    prefix[1] = c;
                }
                rs.append(c);
                continue;
            }
            if (prefix[2] == 0) {
                if (c != prefix[1]) {
                    prefix[2] = c;
                    rs.append(c);
                }
                continue;
            }
            if (prefix[2] != c) {
                prefix[0] = c;
                prefix[1] = 0;
                prefix[2] = 0;
                rs.append(c);
            }
        }
        return rs.toString();
    }
}
