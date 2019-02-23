package com.pikaqiu.concurrency;

import com.pikaqiu.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@NotThreadSafe
public class ConcurrencyTest {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    /**
     * 计数值
     */
    public static int count = 0;

    public static void main(String[] args) throws Exception {
        //定义线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        //信号量                                   并发数
        final Semaphore semaphore = new Semaphore(threadTotal);
        //线程计数器                                           线程计数器计数次数
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        //循环提交任务
        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                //首先引人信号量
                try {
                    //启用 （是否允许范围内 超出范围线程等待）
                    semaphore.acquire();
                    add();
                    //释放
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                //线程计数器 减一
                countDownLatch.countDown();
            });
        }
        //线程计数器等待
        countDownLatch.await();
        //线程池终止
        executorService.shutdown();
        log.info("count:{}", count);
    }

    private static void add() {
        count++;
    }
}
