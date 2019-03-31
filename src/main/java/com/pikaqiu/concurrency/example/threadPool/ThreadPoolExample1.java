package com.pikaqiu.concurrency.example.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ThreadPoolExample1 {
    /**
     * 不建议的单独new Thread 或者 创建无界线程池
     *
     *      构造参数解析
     * corePoolSize 核心线程数 线程小于这个时 直接创建新线程执行 任务
     * maximumPoolSize 最大线程数
     * workQueue 阻塞队列 待执行的任务
     * 当为无界队列  可以提交任意个数任务 且maximumPoolSize 参数无效
     * 当为有界队列  一般为BlockingQueue
     * 当线程数位于 corePoolSize 和 max之间时  只有workQueue满了才会创建新线程
     *
     * keepAliveTime 线程没有任务执行最长保持多久时间终止 unit 时间单位
     * threadFactory 线程工厂，用来创建线程。
     * rejectHandler 当拒绝时的策略（阻塞队列满了 又在提交任务）
     * 1 默认 直接抛出异常 2 调用者所在线程执行任务 3 丢弃队列中最前面任务 4 直接丢弃
     *
     * @param args
     */
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    log.info("task:{}", index);
                }
            });
        }
        executorService.shutdown();
    }
}
