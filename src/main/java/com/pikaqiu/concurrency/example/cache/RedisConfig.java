package com.pikaqiu.concurrency.example.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    @Bean(name = "redisPool")
    public JedisPool jedisPool(@Value("${jedis.host:123.207.5.185}") String host,
                               @Value("${jedis.port:6379}") int port) {
        return new JedisPool(host, port);
    }
}
