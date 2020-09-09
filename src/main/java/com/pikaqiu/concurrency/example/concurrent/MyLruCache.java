package com.pikaqiu.concurrency.example.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p> MyLruCache </p>
 *
 * @author xiaoye
 * @version 1.0
 * @date 2020/9/9 11:14
 */
public class MyLruCache<K, V> {

  /**
   * 缓存的最大容量
   */
  private final int maxCapacity;

  private ConcurrentHashMap<K, V> cacheMap;
  private ConcurrentLinkedQueue<K> keys;
  /**
   * 读写锁
   */
  private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  //写锁 写锁和读锁/写锁互斥
  private Lock writeLock = readWriteLock.writeLock();
  //读锁只和读锁不互斥
  private Lock readLock = readWriteLock.readLock();
  private ScheduledExecutorService scheduledExecutorService;

  public MyLruCache(int maxCapacity) {
    if (maxCapacity < 0) {
      throw new IllegalArgumentException("Illegal max capacity: " + maxCapacity);
    }
    this.maxCapacity = maxCapacity;
    cacheMap = new ConcurrentHashMap<>(maxCapacity);
    keys = new ConcurrentLinkedQueue<>();
    //创建核心线程 = 3 的线程池
    scheduledExecutorService = Executors.newScheduledThreadPool(3);
  }

  public V put(K key, V value) {
    // 加写锁 防止过程中读/写
    writeLock.lock();
    try {
      //1.key是否存在于当前缓存
      if (cacheMap.containsKey(key)) {
        moveToTailOfQueue(key);
        cacheMap.put(key, value);
        return value;
      }
      //2.是否超出缓存容量，超出的话就移除队列头部的元素以及其对应的缓存
      if (cacheMap.size() == maxCapacity) {
        System.out.println("maxCapacity of cache reached");
        removeOldestKey();
      }
      //3.key不存在于当前缓存。将key添加到队列的尾部并且缓存key及其对应的元素
      keys.add(key);
      cacheMap.put(key, value);
      return value;
    } finally {
      writeLock.unlock();
    }
  }

  public V get(K key) {
    //加读锁 防止过程中写
    readLock.lock();
    try {
      //key是否存在于当前缓存
      if (cacheMap.containsKey(key)) {
        // 存在的话就将key移动到队列的尾部
        moveToTailOfQueue(key);
        return cacheMap.get(key);
      }
      //不存在于当前缓存中就返回Null
      return null;
    } finally {
      readLock.unlock();
    }
  }

  public V remove(K key) {
    writeLock.lock();
    try {
      //key是否存在于当前缓存
      if (cacheMap.containsKey(key)) {
        // 存在移除队列和Map中对应的Key
        keys.remove(key);
        return cacheMap.remove(key);
      }
      //不存在于当前缓存中就返回Null
      return null;
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * 将元素添加到队列的尾部(put/get的时候执行)
   */
  private void moveToTailOfQueue(K key) {
    keys.remove(key);
    keys.add(key);
  }

  /**
   * 移除队列头部的元素以及其对应的缓存 (缓存容量已满的时候执行)
   */
  private void removeOldestKey() {
    K oldestKey = keys.poll();
    if (oldestKey != null) {
      cacheMap.remove(oldestKey);
    }
  }
  private void removeAfterExpireTime(K key, long expireTime) {
    //调度延迟任务
    scheduledExecutorService.schedule(() -> {
      //过期后清除该键值对
      cacheMap.remove(key);
      keys.remove(key);
    }, expireTime, TimeUnit.MILLISECONDS);
  }


  public int size() {
    return cacheMap.size();
  }

}
