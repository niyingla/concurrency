package com.pikaqiu.concurrency.example.publish;

import com.pikaqiu.concurrency.annoations.NotRecommend;
import com.pikaqiu.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NotThreadSafe
@NotRecommend
/**
 * 对象未完成构造前  不能将它发布
 * Escape 可能未被初始化就调用了内部类InnerClass
 * 这时候thisCanBeEscape是没有初始化的
 */
public class Escape {

    private int thisCanBeEscape = 0;

    public Escape () {
        new InnerClass();
    }

    private class InnerClass {

        public InnerClass() {
            log.info("{}", Escape.this.thisCanBeEscape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
