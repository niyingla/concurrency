package com.pikaqiu.concurrency.example.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ThreadPoolExample2 {

    public static void main(String[] args) {

        /**
         * 创建定长线程池 超出线程会等待
         *
         * 计算密集型 cpu+1 / cpu*2
         *
         * io 密集型 cpu/(1-阻塞系数 ) 阻塞系数 一般0.8-0.9 约为核数 5-10倍
         *
         * 所有线程池 加起来 不可以太大
         */
        ExecutorService executorService = Executors.newFixedThreadPool(3);

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
