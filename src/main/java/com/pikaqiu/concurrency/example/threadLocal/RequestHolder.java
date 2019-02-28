package com.pikaqiu.concurrency.example.threadLocal;

/**
 * 线程安全
 */
public class RequestHolder {

    /**
     *  ThreadLocal 内部维护了一个map key是当前线程 value是值
     */
    private final static ThreadLocal<Long> requestHolder = new ThreadLocal<>();

    public static void add(Long id) {
        requestHolder.set(id);
    }

    public static Long getId() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }
}
