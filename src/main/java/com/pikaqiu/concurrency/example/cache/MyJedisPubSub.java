package com.pikaqiu.concurrency.example.cache;


import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

@Slf4j
public class MyJedisPubSub extends JedisPubSub {
    @Override
    /** JedisPubSub类是一个没有抽象方法的抽象类,里面方法都是一些空实现
     * 所以可以选择需要的方法覆盖,这儿使用的是SUBSCRIBE指令，所以覆盖了onMessage
     * 如果使用PSUBSCRIBE指令，则覆盖onPMessage方法
     * 当然也可以选择BinaryJedisPubSub,同样是抽象类，但方法参数为byte[]
     **/
    public void onMessage(String channel, String message) {
       log.info(Thread.currentThread().getName() + "-接收到消息:channel=" + channel + ",message=" + message);
        //接收到exit消息后退出
    }
}
