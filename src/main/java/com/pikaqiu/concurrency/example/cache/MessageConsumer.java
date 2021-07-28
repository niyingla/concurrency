package com.pikaqiu.concurrency.example.cache;

import redis.clients.jedis.Jedis;

public class MessageConsumer implements Runnable{
    //频道
    public static final String CHANNEL_KEY = "channel:1";
    //结束程序的消息
    public static final String EXIT_COMMAND = "exit";

    //处理接收消息
    private MyJedisPubSub myJedisPubSub = new MyJedisPubSub();

    //客户端
    private Jedis jedis;

    public MessageConsumer(Jedis jedis) {
        this.jedis = jedis;
    }

    public void consumerMessage() {
        //第一个参数是处理接收消息，第二个参数是订阅的消息频道
        jedis.subscribe(myJedisPubSub, CHANNEL_KEY);
    }

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

}
