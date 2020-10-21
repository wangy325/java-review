package com.wangy.review.concurrency.component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 使用Future&lt;?&gt;构建一个可以取消的任务
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/21 / 12:49
 */
public class CancelableTask {

    static class Cancelable<V> implements Callable<V> {

        @Override
        public V call() throws Exception {
            System.out.println("---");
            int i = 0;
            while (true) {
                i++;
                if (i > 100000) {
                    break;
                }
            }
            return null;
        }

    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        // 可以使用Future<?> 表示没有返回值但可取消的任务
        Future<?> submit = service.submit(new Cancelable<>());
        System.out.println(submit.cancel(true));
        System.out.println(submit.isCancelled());
        System.out.println(submit.isDone());
        service.shutdown();
    }
}
