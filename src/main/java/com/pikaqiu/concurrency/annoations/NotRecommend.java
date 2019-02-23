package com.pikaqiu.concurrency.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 课程里用来标记【不推荐】的类或者写法
 */
//作用的目标 类类型
@Target(ElementType.TYPE)
//注解存在的节点 （当前是只存在源码）
@Retention(RetentionPolicy.SOURCE)
public @interface NotRecommend {

    String value() default "";
}
