package com.pikaqiu.concurrency.example.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pikaqiu.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@ThreadSafe
public class GuavaCacheExample1 {
    /**
     * segment 细粒度锁 数据结构类似于map + 缓存过期 动态加载 等
     * 超过最大值时 适用LRU算法来移除 可以统计命中率 未命中率 异常率
     * @param args
     */
    public static void main(String[] args) {

        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(10) // 最多存放10个数据
                .expireAfterWrite(10, TimeUnit.SECONDS) // 缓存10秒
                .recordStats() // 开启记录状态数据功能
                //指定加载数据方法
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return -1;
                    }
                });

        log.info("{}", cache.getIfPresent("key1")); // null
        cache.put("key1", 1);
        log.info("{}", cache.getIfPresent("key1")); // 1
        cache.invalidate("key1");
        log.info("{}", cache.getIfPresent("key1")); // null

        try {
            log.info("{}", cache.get("key2")); // -1
            cache.put("key2", 2);
            log.info("{}", cache.get("key2")); // 2

            log.info("{}", cache.size()); // 1

            for (int i = 3; i < 13; i++) {
                cache.put("key" + i, i);
            }
            log.info("{}", cache.size()); // 10

            log.info("{}", cache.getIfPresent("key2")); // null

            Thread.sleep(11000);

            log.info("{}", cache.get("key5")); // -1

            // 打印命中数率和未命中数
            log.info("{},{}", cache.stats().hitCount(), cache.stats().missCount());
            // 打印命中率和未命中率
            log.info("{},{}", cache.stats().hitRate(), cache.stats().missRate());
        } catch (Exception e) {
            log.error("cache exception", e);
        }
    }
}
