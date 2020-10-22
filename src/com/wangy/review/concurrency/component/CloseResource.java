package com.wangy.review.concurrency.component;

import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 关闭使任务阻塞的底层资源使线程响应中断
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/22 / 21:34
 */
public class CloseResource {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        InputStream socketInput = new Socket("localhost", 8080).getInputStream();
        exec.execute(new Interrupting.IOBlocked(socketInput));
        exec.execute(new Interrupting.IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println("Shutting down all threads");
        // 两个任务都无法响应中断
        exec.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + socketInput.getClass().getName());
        // 关闭资源可以使线程响应中断
        socketInput.close(); // Releases blocked thread
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + System.in.getClass().getName());
        System.in.close(); // Releases blocked thread
    }
}
/* Output: (85% match)
Waiting for read():
Waiting for read():
Shutting down all threads
Closing java.net.SocketInputStream
Interrupted from blocked I/O
Exiting IOBlocked.run()
Closing java.io.BufferedInputStream
Exiting IOBlocked.run()
*///:~

