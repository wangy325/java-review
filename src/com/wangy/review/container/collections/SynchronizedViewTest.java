package com.wangy.review.container.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程安全的集合中“线程安全”的含义是多线程对集合元素的操作是同步的<br>
 * <p>
 * 但是
 * <pre>
 *     private void task(List<Integer> list) {
 *         for (int i = 0; i < 50; i++) {
 *             cal_count.getAndIncrement();
 *             synchronized (this) {
 *                 list.set(0, list.get(0) + i);
 *             }
 *         }
 *     }
 * </pre>
 * 方法中对<code>list</code>的更改还是需要使用同步，不仅对{@link ArrayList}如此，
 * 对{@link Collections#synchronizedList(List)}和{@link Vector}都是如此<br>
 * 如果不使用同步，那么在
 * <pre>
 *     private void test(List<Integer> list) throws InterruptedException {
 *         int count = 0;
 *         System.out.println("程序10s后自动退出");
 *         ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
 *         service.schedule(() -> System.exit(0), 10, TimeUnit.SECONDS);
 *         for (; ; ) {
 *             count++;
 *             unsafeIncrAndReplace(list);
 *             if (list.get(0) != 2450) {
 *                 System.out.println(list.get(0) + ": " + cal_count + ", 第" + count + "次出现讹误");
 *                 break;
 *             }
 *         }
 *     }
 * </pre>
 * 方法中的for循环始终会出现讹误<br>
 * 对于线程不安全的{@link ArrayList}来说，这是容易理解的，因为多个线程同时对集合内容进行更改会引发安全问题<br>
 * 但对于线程安全的同步视图和{@link Vector}来说，这应该如何理解？<br>
 * 问题就出在
 * <pre>
 *     list.set(0, list.get(0) + i);
 * </pre>
 * 上，上述逻辑等价于
 * <PRE>
 *     int y = list.get(0) + i;
 *     // 可以被cpu调度器剥夺运行权限
 *     list.set(0,y);
 * </PRE>
 * 这样会导致集合的值“滞后”更新，因此测试下来，发现出现讹误的情况都是最后的值&lt;2450
 * </p>
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/9 / 11:23
 */
public class SynchronizedViewTest {


    /**
     * 同步集合-- 对集合元素的操作是同步的，使用synchronized关键字获取this的锁
     */
    private static List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>() {{
        add(0);
    }});
    /**
     * 非同步集合
     */
    private static List<Integer> unsafe_list = new ArrayList<Integer>() {{
        add(0);
    }};
    /**
     * 同步集合
     */
    private static Vector<Integer> vector = new Vector<Integer>() {{
        add(0);
    }};

    /**原子类计数器*/
    private AtomicInteger cal_count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        SynchronizedViewTest svt = new SynchronizedViewTest();
//        svt.test(synchronizedList);
//        svt.test(vector);
        svt.test(unsafe_list);
    }

    private void test(List<Integer> list) throws InterruptedException {
        int count = 0;
        System.out.println("程序10s后自动退出");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> System.exit(0), 10, TimeUnit.SECONDS);
        for (; ; ) {
            count++;
            unsafeIncrAndReplace(list);
            if (list.get(0) != 2450) {
                // 这里获取到的值一定是任务执行完毕之后的最新值
                // 如果任务的操作不是同步的，那么此循环一定会break
                System.out.println(list.get(0) + ": " + cal_count + ", 第" + count + "次出现讹误");
                System.exit(1);
            }
        }
    }

    /**
     * 使用多线程对集合内容进行修改，查看不同类型集合的表现
     *
     * @param list
     */
    private void unsafeIncrAndReplace(List<Integer> list) throws InterruptedException {
        list.set(0, 0);
        cal_count.set(0);
        ExecutorService executorService = Executors.newCachedThreadPool();

        // thread-1
        executorService.execute(() -> task(list));

        // thread-2
        executorService.execute(() -> task(list));

        executorService.shutdown();
        // 阻塞当前线程直到所有任务执行完毕/或超时
        executorService.awaitTermination(2, TimeUnit.SECONDS);
//        System.out.println(executorService.isTerminated()); // true
    }

    private void task(List<Integer> list) {
        for (int i = 0; i < 50; i++) {
            cal_count.getAndIncrement();
            // 需要同步
            synchronized (this) {
                list.set(0, list.get(0) + i);
            }
        }
    }
}
