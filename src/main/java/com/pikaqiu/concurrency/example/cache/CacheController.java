package com.pikaqiu.concurrency.example.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/set")
    @ResponseBody
    public String set(@RequestParam("k") String k, @RequestParam("v") String v) throws Exception {
        redisClient.set(k, v);
        return "SUCCESS";
    }

    @RequestMapping("/get")
    @ResponseBody
    public String get(@RequestParam("k") String k) throws Exception {
        return redisClient.get(k);
    }


    @GetMapping("pushMsg")
    public void pushMsg(@RequestParam String msg) {
        Jedis resource = jedisPool.getResource();
        resource.publish(MessageConsumer.CHANNEL_KEY, msg);

    }

    @PostConstruct
    public void testSub() {
        MessageConsumer messageConsumer1 = new MessageConsumer(jedisPool.getResource());
        MessageConsumer messageConsumer2 = new MessageConsumer(jedisPool.getResource());
        Thread t1 = new Thread(messageConsumer1, "thread5");
        Thread t2 = new Thread(messageConsumer2, "thread6");
        t1.start();
        t2.start();
    }
}
