package com.wangy.algorithm.recursive;

import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * 【递归问题】：如有N个台阶，每次可以走1阶或者2阶，求走完N个台阶的所有方法
 *
 * <p>
 * 如何判断一个问题可以归纳为递归问题？
 * <ul>
 *     <li>
 *         1. 一个问题可以分解为另一个或多个问题的解；
 *     </li>
 *     <li>
 *         2. 原问题与被分解的问题除了数据规模不一样，解决思路一样；
 *     </li>
 *     <li>
 *         3. 存在边界条件，可以终止递归；
 *     </li>
 * </ul>
 * </p>
 * <p>
 * 结合上述分析，来分析这个台阶问题，若记走完N阶有F(N)种方法。
 * <p>
 * 已知第一步可以走1阶或者2阶，那么走完剩余的台阶有F(N-1)或F(N-2)种方法，那么有
 * <pre>
 *     F(N) = F(N-1) + F(N-2) (N>2)
 * </pre>
 *
 * <p>
 * 现在看看边界条件，当N=1和N=2时，F(1)=1，F(2)=2。
 * <p>
 * 因此上述问题的解可记为：
 * <pre>
 *     /    F(1) = 1
 *    |     F(2) = 2
 *     \    F(N) = F(N-1) + F(N-2) (N>2)
 * </pre>
 * <p>
 * 以上被称为递推公式，只要有了递推公式，很容易将递归问题用代码实现：
 * <pre>
 *     int solution(int n){
 *         if(n == 1) return 1;
 *         if(n == 2) return 2;
 *         return solution(n-1) + solution(n-2);
 *     }
 * </pre>
 *
 * <p>
 * 不过，递归也会存在问题：
 * <ul>
 *     <li>
 *         1. 如果递归深度很大，可能会造成栈溢出(StackOverFlow)；
 *     </li>
 *     <li>
 *         2. 递归过程可能存在重复计算的情形，这样也会造成时间和空间的浪费；
 *     </li>
 * </ul>
 * <p>
 * 关于第一个问题，可以在编码时限制递归深度。不过如此处理可能有悖于业务逻辑，需要具体考虑。
 * <p>
 * 关于重复计算，还以上述问题为例：
 * <pre>
 *                    F(2)
 *                  /
 *              F(4)
 *            /     \
 *          /        F(3) ...
 *     F(6)
 *          \        F(3) ...
 *           \     /
 *            F(5)
 *                \
 *                  F(4) ...
 * </pre>
 * <p>
 * 可以看到，F(4)和F(3)都计算了多次。可以引入一个表，用于记录已经计算过的值，这样可以减少计算的次数。
 * <p>
 * <b>一般地，所有的递归问题，都可以转化为使用一般代码实现。</b>
 *
 * @author wangy
 * @version 1.0
 * @date 2021/7/2 / 10:47
 */
public class Steps {
    private static HashMap resultSet = new HashMap();

    // 递归实现
    // 时间复杂度O(n)，空间复杂度O(n)
    static int solution(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        if (resultSet.containsKey(n))
            return (int) resultSet.get(n);
        int res = solution(n - 1) + solution(n - 2);
        resultSet.put(n, res);
        return res;
    }

    // 非递归实现
    // 时间复杂度O(n)，空间复杂度O(1)
    static int solution2(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        int fn_1 = 2;
        int fn_2 = 1;
        int res = 0;
        for (int i = 3; i <= n ; i++){
            res = fn_1 + fn_2;
            fn_2 = fn_1;
            fn_1 = res;
        }
        return res;
    }

    public static void main(String[] args) {
        int n = 6;
        System.out.println(solution(n));
        Assert.isTrue(solution(n) == solution2(n));
    }

}
