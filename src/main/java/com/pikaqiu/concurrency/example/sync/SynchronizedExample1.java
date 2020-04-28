package com.pikaqiu.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SynchronizedExample1 {

    /**
     * synchronized全流程
     * 1 在代码进入同步块的时候，如果同步对象锁状态为无锁状态，前线程的栈帧中建立一个名为锁记录
     * （Lock Record），拷贝对象头中的Mark Word复制到锁记录中,此时处于偏向锁
     * 2 使用CAS操作尝试将对象的Mark Word更新为指向Lock Record的指针，
     * 并将Lock record里的owner指针指向object mark word。
     * 3 如果更新成功，那么这个线程就拥有了该对象的锁，
     * 并且对象Mark Word的锁标志位设置为“00”，即表示此对象处于轻量级锁定状态
     * 4 如果这个更新操作失败了，说明多个线程竞争锁，轻量级锁就要膨胀为重量级锁
     * 在锁膨胀时， 被锁对象的 mark word 会被通过 CAS 操作尝试更新为一个数据结构的指针，
     * 这个数据结构中进一步包含了指向操作系统互斥量(mutex) 和 条件变量（condition variable） 的指针
     *
     * 修饰一个代码块
     * 只作用于当前对象 不同对象不影响
     * 当代码块包含了全部方法代码时 两种用法效果等同
     * synchronized不属于方法声明的一部分 继承无效
     * @param j
     */
    public void test1(int j) {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                log.info("test1 {} - {}", j, i);
            }
        }
    }

    /**
     * 修饰一个方法
     * 只作用于当前对象 不同对象不影响
     * @param j
     */
    public synchronized void test2(int j) {
        for (int i = 0; i < 10; i++) {
            log.info("test2 {} - {}", j, i);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample1 example1 = new SynchronizedExample1();
        SynchronizedExample1 example2 = new SynchronizedExample1();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            example1.test2(1);
        });
        executorService.execute(() -> {
            example2.test2(2);
        });
        executorService.shutdown();
    }
}
