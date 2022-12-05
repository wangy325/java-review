package com.wangy.algorithm;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

/**
 * 链接：<a href = "https://www.nowcoder.com/questionTerminal/c0803540c94848baac03096745b55b9b?f=discussion">
 *     我叫王大锤，是一名特工。</a><br>
 * 我刚刚接到任务：在字节跳动大街进行埋伏，抓捕恐怖分子孔连顺。和我一起行动的还有另外两名特工，我提议
 * <p>
 * 1. 我们在字节跳动大街的N个建筑中选定3个埋伏地点。<br>
 * 2. 为了相互照应，我们决定相距最远的两名特工间的距离不超过D。<br>
 * <p>
 * 我特喵是个天才! 经过精密的计算，我们从X种可行的埋伏方案中选择了一种。这个方案万无一失，颤抖吧，孔连顺！<br>
 * ……
 * 万万没想到，计划还是失败了，孔连顺化妆成小龙女，混在cosplay的队伍中逃出了字节跳动大街。只怪他的伪装太成功了，就是杨过本人来了也
 * 发现不了的！
 * <p>
 * 请听题：给定N（可选作为埋伏点的建筑物数）、D（相距最远的两名特工间的距离的最大值）以及可选建筑的坐标，计算在这次行动中，大锤的小队
 * 有多少种埋伏选择。
 * <p>
 * 注意：<br>
 * 1. 两个特工不能埋伏在同一地点<br>
 * 2. 三个特工是等价的：即同样的位置组合(A, B, C) 只算一种埋伏方法，不能因“特工之间互换位置”而重复使用<br>
 * <p>
 * 输入描述:<br>
 * 第一行包含空格分隔的两个数字 N和D(1 ≤ N ≤ 1000000; 1 ≤ D ≤ 1000000)
 * <p>
 * 第二行包含N个建筑物的的位置，每个位置用一个整数（取值区间为[0, 1000000]）表示，从小到大排列（将字节跳动大街看做一条数轴）
 * <p>
 * <p>
 * 输出描述:<br>
 * 一个数字，表示不同埋伏方案的数量。结果可能溢出，请对 99997867 取模<br>
 * 示例1<br>
 * 输入
 * <pre>
 * 4 3
 * 1 2 3 4
 * </pre>
 * 输出
 * <pre>
 * 4
 * </pre>
 * 说明<br>
 * 可选方案 (1, 2, 3), (1, 2, 4), (1, 3, 4), (2, 3, 4)<br>
 * 示例2<br>
 * 输入
 * <pre>
 * 5 19
 * 1 10 20 30 50
 * </pre>
 * 输出
 * <pre>
 * 1
 * </pre>
 * 说明<br>
 * 可选方案 (1, 10, 20)
 * <p>
 * todo: 这个代码算法复杂度过高，不符合！
 *
 * @author wangy
 * @version 1.0
 * @date 2020/12/2 / 13:19
 */
public class Plots {

    static int N;
    static int D;
    static Queue<Integer> builds = new ArrayDeque<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        if (N < 3 || N > 100000) return;
        D = scanner.nextInt();
        if (D > 1000000) return;
        for (int i = 0; i < N; i++) {
            builds.add(scanner.nextInt());
        }
        scanner.close();
//        System.out.println(builds);
        System.out.println(ap() % 99997867);
    }

    static int ap() {
        Integer[] plots = new Integer[3];
        int avi = 0;

        top:
        while (builds.size() > 2) {
            plots[0] = builds.poll();
            Queue<Integer> q2 = new ArrayDeque<>(builds);
            while (q2.size() > 1) {
                plots[1] = q2.poll();
                if (plots[1] - plots[0] > D)
                    continue top;
                Queue<Integer> q3 = new ArrayDeque<>(q2);
                while (!q3.isEmpty()) {
                    plots[2] = q3.poll();
                    if (plots[2] - plots[1] > D)
                        continue top;
                    if (plots[2] - plots[0] <= D) {
                        avi++;
                    }
                }
            }
        }
        return avi;
    }
}
