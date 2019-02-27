package com.pikaqiu.concurrency.example.singleton;

import com.pikaqiu.concurrency.annoations.ThreadSafe;

/**
 * 饿汉模式
 * 单例实例在类装载时进行创建
 * 静态域和静态代码块 一定要注意前后循序
 * 因为前后顺序就是他们的赋值循序
 */
@ThreadSafe
public class SingletonExample6 {

    // 私有构造函数
    private SingletonExample6() {

    }

    // 单例对象
    private static SingletonExample6 instance = null;

    static {
        instance = new SingletonExample6();
    }

    // 静态的工厂方法
    public static SingletonExample6 getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(getInstance().hashCode());
        System.out.println(getInstance().hashCode());
    }
}
