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
        //请求结束时 请务必释放，也就是删除value（类会一直伴随着项目），然后造成内存泄漏。
        requestHolder.remove();
    }
}
