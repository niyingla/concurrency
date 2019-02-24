package com.pikaqiu.concurrency.example.atomic;

import com.pikaqiu.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * compareAndSet方法 比较第一个值和对象中的值
 * 一致就更新第二个值到对象中 不一致就什么也不做
 *
 */
@Slf4j
@ThreadSafe
public class AtomicExample4 {

    /**
     * 创建原子引用类 类型为Integer
     */
    private static AtomicReference<Integer> count = new AtomicReference<>(0);

    public static void main(String[] args) {
        count.compareAndSet(0, 2); // 2
        count.compareAndSet(0, 1); // no
        count.compareAndSet(1, 3); // no
        count.compareAndSet(2, 4); // 4
        count.compareAndSet(3, 5); // no
        log.info("count:{}", count.get());
    }
}
