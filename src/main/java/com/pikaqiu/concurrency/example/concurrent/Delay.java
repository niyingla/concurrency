package com.pikaqiu.concurrency.example.concurrent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @program: concurrency
 * @description:
 * @author: xiaoye
 * @create: 2019-04-02 18:54
 **/
public class Delay {
    /**
     * 延迟队列示例
     */
    private static DelayQueue<DelayTask> delayQueue = new DelayQueue<>();

    static class DelayTask implements Delayed {
        /**
         * 延迟时间
         */
        private final long delay;
        /**
         * 到期时间
         */
        private final long expire;
        /**
         * 数据
         */
        private final String msg;
        /**
         * 创建时间
         */
        private final long now;

        /**
         * 初始化 DelayTask 对象
         *
         * @param delay 延迟时间 单位：微妙
         * @param msg   业务信息
         */
        DelayTask(long delay, String msg) {
            //延迟时间
            this.delay = delay;
            // 业务信息
            this.msg = msg;
            this.now = Instant.now().toEpochMilli();
            // 到期时间 = 当前时间+延迟时间
            this.expire = now + delay;
        }

        /**
         * 获取延迟时间
         *
         * @param unit 单位对象
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            //时间单元转化unit.convert(时间数额,时间单位) 转化为 MILLISECONDS（毫秒）
            //方法返回0或负数将会执行 expire 过期的时间 - 当前时间
            return unit.convert(expire - Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS);
        }

        /**
         * 比较器
         * 比较规则：延迟时间越长的对象越靠后
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            // compare zero ONLY if same object 同一对象返回0
            if (o == this) {
                return 0;
            }
            //不是同一对象 用延迟时间进行比较
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "DelayTask{" +
                    "delay=" + delay +
                    ", expire=" + expire +
                    ", msg='" + msg + '\'' +
                    ", now=" + now +
                    '}';
        }
    }

    /**
     * 生产者线程
     *
     * @param args
     */
    public static void main(String[] args) {
        initConsumer();
        try {
            // 等待消费者初始化完毕
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        delayQueue.add(new DelayTask(1000, "Task1"));
        delayQueue.add(new DelayTask(2000, "Task2"));
        delayQueue.add(new DelayTask(3000, "Task3"));
        delayQueue.add(new DelayTask(4000, "Task4"));
        delayQueue.add(new DelayTask(5000, "Task5"));
    }

    /**
     * 初始化消费者线程
     */
    private static void initConsumer() {
        //创建Runnable实例
        Runnable task = () -> {
            //实例中循环运行
            while (true) {
                try {
                    System.out.println("尝试获取延迟队列中的任务。" + LocalDateTime.now());
                    System.out.println(delayQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //创建运行线程
        Thread consumer = new Thread(task);
        consumer.start();
    }
}
