package com.pikaqiu.concurrency.example.lock;

import com.pikaqiu.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.StampedLock;

@Slf4j
@ThreadSafe
public class LockExample5 {
    /**
     * StampedLock则提供了一种乐观的读策略,这种乐观策略的锁非常类似于无锁的操作，使得乐观锁完全不会阻塞写线程。
     */

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    public static int count = 0;

    private final static StampedLock lock = new StampedLock();

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
        //lock.tryOptimisticRead 试图尝试一次乐观读 返回一个类似于时间戳的邮戳整数stamp 这个stamp就可以作为这一个所获取的凭证
        long stamp = lock.writeLock();
        try {
            count++;
        } finally {
            lock.unlock(stamp);
        }

    }

    private static void add2(){
        // 试图尝试一次乐观读 返回一个类似于时间戳的邮戳整数stamp 这个stamp就可以作为这一个所获取的凭证
        long stamp = lock.tryOptimisticRead();
        // 如果stamp没有被修改过,则任务这次读取时有效的,
        // 因此就可以直接return了,反之,如果stamp是不可用的,则意味着在读取的过程中,可能被其他线程改写了数据,因此,有可能出现脏读,
        // 如果如果出现这种情况,我们可以像CAS操作那样在一个死循环中一直使用乐观锁,知道成功为止
        if (lock.validate(stamp)) {
            //也可以升级锁的级别,这里我们升级乐观锁的级别,将乐判断这个stamp是否在读过程发生期间被修改过观锁变为悲观锁,
            // 如果当前对象正在被修改,则读锁的申请可能导致线程挂起.
            stamp = lock.readLock();
            try {
                count++;
            } finally {
                //退出临界区,释放读锁
                lock.unlockRead(stamp);
            }
        }
    }
}
