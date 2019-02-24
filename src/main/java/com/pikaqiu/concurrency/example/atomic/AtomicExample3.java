package com.pikaqiu.concurrency.example.atomic;

import com.pikaqiu.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
@ThreadSafe
public class AtomicExample3 {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;
    /**
     * jdk8新增 一定程度上减小并发压力
     * 原Atomic类基于死循环对比后写入（低并发推荐）
     * 当并发不高时写入概率很大
     * 并发变高后效率明显变低 这时候可以考虑 LongAdder进行替换
     * 基于热点数据分离 比如将value分离成一个数组
     * 通过算法预测某一个进行计数最后结果为数组求和累加（分散了更新压力）
     */
    public static LongAdder count = new LongAdder();

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}", count);
    }

    private static void add() {
        count.increment();
//        增加指定数count.add(10);
    }
}
