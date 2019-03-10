package com.pikaqiu.concurrency.example.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LockExample6 {

    public static void main(String[] args) {
        // 内部包含两个队列 AQS执行队列 和 Condition等待队列
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        new Thread(() -> {
            try {
                //加入AQS等待队列
                reentrantLock.lock();
                log.info("wait signal"); // 1
                //AQS队列移除且加入condition的等待队列
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("get signal"); // 4
            //删除AQS中的数据
            reentrantLock.unlock();
        }).start();
        //复制一份
        new Thread(() -> {
            try {
                reentrantLock.lock();
                log.info("wait signal"); // 1
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("get signal"); // 4
            reentrantLock.unlock();
        }).start();
        new Thread(() -> {
            //线程1 因为释放锁被唤醒 加入AQS
            reentrantLock.lock();
            log.info("get lock"); // 2
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //线程1入AQS
            condition.signalAll();
            log.info("send signal ~ "); // 3
            //依序唤醒AQS
            reentrantLock.unlock();
        }).start();
    }
}
