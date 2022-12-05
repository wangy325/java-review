package com.wangy.algorithm;

import java.util.Scanner;

/**
 * 来源：https://www.nowcoder.com/questionTerminal/f9c6f980eeec43ef85be20755ddbeaf4
 * <p>
 * 王强今天很开心，公司发给N元的年终奖。王强决定把年终奖用于购物，他把想买的物品分为两类：主件与附件，附件是从属于某个主件的，
 * 下表就是一些主件与附件的例子：
 * <table>
 * <tr>
 *     <th>主件</th>
 *     <th>附件</th>
 * </tr>
 * <tr>
 *     <td>电脑</td>
 *     <td>打印机，扫描仪</td>
 * </tr>
 * <tr>
 *     <td>书柜</td>
 *     <td>图书</td>
 * </tr>
 * <tr>
 *     <td>书桌</td>
 *     <td>台灯，文具</td>
 * </tr>
 * <tr>
 *     <td>椅子</td>
 *     <td>无</td>
 * </tr>
 * </table>
 * <p>
 * 如果要买归类为附件的物品，必须先买该附件所属的主件。每个主件可以有 <b>0 个、 1 个或 2 个附件</b>。附件不再有从属于自己的附件。
 * 王强想买的东西很多，为了不超出预算，他把每件物品规定了一个重要度，分为 5 等：用整数 1 ~ 5 表示，第 5 等最重要。
 * 他还从因特网上查到了每件物品的价格（<b>都是 10 元的整数倍</b>）。他希望在不超过 N 元（可以等于 N 元）的前提下，
 * 使每件物品的价格与重要度的乘积的总和最大。
 * <p>
 *     设第 j 件物品的价格为 v[j] ，重要度为 w[j] ，共选中了 k 件物品，编号依次为 j 1 ， j 2 ，……， j k ，则所求的总和为：
 * <code>v[j 1 ]*w[j 1 ]+v[j 2 ]*w[j 2 ]+ … +v[j k ]*w[j k ]</code>。（其中 * 为乘号）
 * <p>
 * 请你帮助王强设计一个满足要求的购物单。
 * <p>
 * <p>
 * 输入描述:
 * 输入的第 1 行，为两个正整数，用一个空格隔开：N m（其中 N （ <32000 ）表示总钱数， m （ <60 ）为希望购买物品的个数。）
 * <p>
 * 从第 2 行到第 m+1 行，第 j 行给出了<b>编号为 j-1 </b>的物品的基本数据，每行有 3 个非负整数 v p q
 * <p>
 * （其中 v 表示该物品的价格（ v<10000 ）， p 表示该物品的重要度（ 1 ~ 5 ）， q 表示该物品是主件还是附件。如果 q=0 ，
 * 表示该物品为主件，如果 q>0 ，表示该物品为附件， q 是所属<b>主件的编号</b>）
 * <p>
 * 输出描述:
 *  输出文件只有一个正整数，为不超过总钱数的物品的价格与重要度乘积的总和的最大值（ <200000 ）。
 * <p>
 * 输入示例
 * <pre>
 * 1000 7
 * 800 2 0
 * 400 5 1
 * 300 5 1
 * 400 3 0
 * 200 4 4
 * 500 2 0
 * 300 3 6
 * </pre>
 * 输出示例
 * <pre>
 * 2200
 * </pre>
 *
 * @author wangy
 * @version 1.0
 * @date 2020/12/4 / 18:09
 */
public class PackIssue2 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int N = in.nextInt(); // price limit
        if (N >= 32000) return;
        int m = in.nextInt(); // goods want buy
        if (m >= 60) return;

        Tar[] tars = new Tar[m + 1]; // index from 1

        for (int i = 1; i < m + 1; i++) {
            int v = in.nextInt();
            int p = in.nextInt();
            int q = in.nextInt();
            Tar tar = new Tar(v, p, q > 0);
            if (q > 0) {
                if (tars[q].a1 == 0) {
                    tars[q].setA1(i);
                } else {
                    tars[q].setA2(i);
                }
            }
            // add all goods to Tar[]
            tars[i] = tar;
        }

        // dp
        int[][] dp = new int[m + 1][N + 1];

        for (int i = 1; i < m + 1; i++) {
            Tar tar = tars[i];
            int vi = tar.v;
            for (int j = 10; j < N + 1; j += 10) {
                if (tar.isAttach) {
                    // skip attachments
                    dp[i][j] = dp[i - 1][j];
                    continue;
                }
                if (j >= vi) {
                    // only main
                    int dp1 = vi * tar.p;
                    int dp2 = dp1, dp3 = dp1, dp4 = dp1;
                    int lv2 = vi, lv3 = vi, lv4 = vi;
                    int lj = j - vi;
                    // main + attachment1
                    if (tar.a1 > 0) {
                        Tar a1 = tars[tar.a1];
                        if (lj >= a1.v) {
                            dp2 += a1.v * a1.p;
                            lv2 += a1.v;
                        }
                    }
                    // main + attachment2
                    if (tar.a2 > 0) {
                        Tar a2 = tars[tar.a2];
                        if (lj >= a2.v) {
                            dp3 += a2.v * a2.p;
                            lv3 += a2.v;
                        }
                    }
                    // main + attachment1 + attachment2
                    if (tar.a1 > 0 && tar.a2 > 0) {
                        Tar a1 = tars[tar.a1];
                        Tar a2 = tars[tar.a2];
                        if (lj >= a1.v) {
                            dp4 += a1.v * a1.p;
                            lj -= a1.v;
                            lv4 += a1.v;
                            if (lj >= a2.v) {
                                dp4 += a2.v * a2.p;
                                lv4 += a2.v;
                            }
                        }
                    }
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - vi] + dp1);
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - lv2] + dp2);
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - lv3] + dp3);
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - lv4] + dp4);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        System.out.println(dp[m][N]);
    }


}

class Tar {
    int v; // price
    int p; // priority, from 1-5
    boolean isAttach;
    int a1; // index of attachment 1 in Tar[]
    int a2; // index of attachment 2 in Tar[]

    public Tar(int p, int w, boolean isAttach) {
        this.v = p;
        this.p = w;
        this.isAttach = isAttach;
    }

    public void setA1(int a1) {
        this.a1 = a1;
    }

    public void setA2(int a2) {
        this.a2 = a2;
    }
}
