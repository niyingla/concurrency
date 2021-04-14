package com.pikaqiu.concurrency.example.atomic;

import com.pikaqiu.concurrency.annoations.ThreadSafe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author 10479
 */

@Slf4j
@ThreadSafe
public class AtomicExample5 {

    /**
     * 创建AtomicInteger字段修改类 并指定修改的字段
     * 使用方法类似于AtomicInteger
     */
    private static AtomicIntegerFieldUpdater<AtomicExample5> updater = AtomicIntegerFieldUpdater
            .newUpdater(AtomicExample5.class, "count");

    /**
     * 给变量提供get方法
     * 字段要求 volatile修饰 且不是static
     */
    @Getter
    public volatile int count = 100;

    public static void main(String[] args) {
        //指定需要更新值的对象
        AtomicExample5 example5 = new AtomicExample5();
        //和目标值进行比较 一致就修改
        if (updater.compareAndSet(example5, 100, 120)) {
            log.info("update success 1, {}", example5.getCount());
        }
        //和目标值进行比较 一致就修改
        if (updater.compareAndSet(example5, 100, 120)) {
            log.info("update success 2, {}", example5.getCount());
        } else {
            log.info("update failed, {}", example5.getCount());
        }
    }
}
