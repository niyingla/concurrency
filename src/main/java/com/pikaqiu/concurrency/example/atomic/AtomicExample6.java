package com.pikaqiu.concurrency.example.atomic;

import com.pikaqiu.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@ThreadSafe
public class AtomicExample6 {

    /**
     * 此类可以用来判断 多线程环境下某个单次操作是否执行过
     * 还有 AtomicIntegerArray 操作的是一个数组此没操作需要传入一个索引即可
     * 还有 AtomicStampedReference 每次操作不仅比较值还会比较版本号 解决ABA问题
     * compareAndSet也就是CAS 是先比较底层实际值和当前获取值一致 才进行运算并赋值
     */
    private static AtomicBoolean isHappened = new AtomicBoolean(false);

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {
            //测试多线程并发执行 某一个单次操作
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    test();
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("isHappened:{}", isHappened.get());
    }

    private static void test() {
        if (isHappened.compareAndSet(false, true)) {
            log.info("execute-------test");
        }
    }
}
