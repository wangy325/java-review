package com.wangy.review.algorithm;

/**
 * <p>
 * 动态规划算法之背包问题：
 * <p>
 * 给定n种物品和一背包。物品 i 的重量似乎 wi，其价值为 vi，背包的容量为 c。问应该如何选择装入背包中的物品，
 * 使得装入背包中物品的总价值最大？
 *
 * @author wangy
 * @version 1.0
 * @date 2020/12/3 / 19:00
 */
public class PackIssue {

    public static void main(String[] args) {
        Good[] gs = new Good[]{
            null,
            new Good(5, 4000),
            new Good(1, 2000),
            new Good(4, 1500),
            new Good(2, 8000),
            new Good(1, 7000),
            new Good(2, 3000),
        };
        int N = 10; // 背包最大容量
        // 物品数, 空一行是为了防止计算dp时数组指针越界
        // 不然计算dp时i = 0需要单独讨论
        int m = gs.length - 1;

        // 最大价值表是一张N*m的二维表格，表格的每一格数据dp[i][j]表示i件物品放入容量为j的背包中
        // 所能获取的最大价值，这个最大价值的形成有2个条件：物品i要么放入背包，要么不放入背包
        // 典型的dp公式
        // dp[i][j] = max{dp[i-1][j], dp[i-1,j-w[i]] + v[i]}
        // N也无需从0开始，没有意义
        int[][] dp = new int[m + 1][N + 1];
        for (int i = 1; i < m + 1; i++) {
            int w = gs[i].w;
            for (int j = 1; j < N + 1; j++) {
                if (j >= w) {
                    dp[i][j] = Math.max(
                        dp[i - 1][j],
                        dp[i - 1][j - w] + gs[i].v
                    );
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        for (int i = 1; i < m + 1; i++) {
            for (int j = 1; j <= N; j++) {
                System.out.printf("%5d\t", dp[i][j]);
            }
            System.out.println();
        }
        System.out.println(dp[m][N]);
    }


}

class Good {
    // 物品重量
    int w;
    // 物品价值
    int v;

    public Good(int w, int v) {
        this.w = w;
        this.v = v;
    }
}
