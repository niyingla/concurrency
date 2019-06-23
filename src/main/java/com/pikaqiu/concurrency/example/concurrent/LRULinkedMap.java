package com.pikaqiu.concurrency.example.concurrent;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: concurrency
 * @description:
 * @author: xiaoye
 * @create: 2019-05-09 15:05
 **/
public class LRULinkedMap<K,V> extends LinkedHashMap<K,V> {


    /**
     * 有初始容器大小的构造
     * @param initialCapacity
     * @param cacheSize
     */
    public LRULinkedMap(int initialCapacity, int cacheSize) {
        //初始大小            超过0.75扩容          根据访问排序
        super(initialCapacity,0.75F,true);
        this.cacheSize = cacheSize;
    }

    /**
     * 无初始容器大小的构造
     * @param cacheSize
     */
    public LRULinkedMap(int cacheSize) {
        //初始大小            超过0.75扩容          根据访问排序
        super(12,0.75F,true);
        this.cacheSize = cacheSize;
    }

    /**
     * 最大缓存大小
     */
    private int cacheSize;

    /**
     * 是否删除老元素
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        //超额删除
        if (cacheSize + 1 == size()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        LRULinkedMap<Integer, Integer> integerIntegerLRULinkedMap = new LRULinkedMap<>(5);
        integerIntegerLRULinkedMap.put(1, 2);
        integerIntegerLRULinkedMap.put(3, 2);
        integerIntegerLRULinkedMap.put(2, 2);
        integerIntegerLRULinkedMap.put(5, 2);
        integerIntegerLRULinkedMap.get(1);
        integerIntegerLRULinkedMap.put(7, 2);
        integerIntegerLRULinkedMap.put(6, 2);
        integerIntegerLRULinkedMap.put(8, 2);

    }
}
