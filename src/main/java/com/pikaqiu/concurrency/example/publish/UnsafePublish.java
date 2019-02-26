package com.pikaqiu.concurrency.example.publish;

import com.pikaqiu.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@NotThreadSafe
/**
 * 多线程调用时 可以拿到states数组的引用
 * 这时候可能被其他线程修改内部数据
 */
public class UnsafePublish {

    private String[] states = {"a", "b", "c"};

    public String[] getStates() {
        return states;
    }

    public static void main(String[] args) {
        UnsafePublish unsafePublish = new UnsafePublish();
        log.info("{}", Arrays.toString(unsafePublish.getStates()));

        unsafePublish.getStates()[0] = "d";
        log.info("{}", Arrays.toString(unsafePublish.getStates()));
    }
}
