package com.pikaqiu.concurrency.example.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/hystrix2")
//@DefaultProperties(defaultFallback = "defaultFail")
public class HystrixController2 {
    /**
     * queueSizeRejectionThreshold：由于 maxQueueSize 
     * 值在线程池被创建后就固定了大小，如果需要动态修改队列长度的话可以设置此值，
     * 即使队列未满，队列内作业达到此值时同样会拒绝请求。此值默认是 5，所以有时候只设置了 
     * maxQueueSize 也不会起作用。
     *
     * keepAliveTimeMinutes：由上面的 maximumSize，
     * 我们知道，线程池内核心线程数目都在忙碌，再有新的请求到达时，
     * 线程池容量可以被扩充为到最大数量，等到线程池空闲后，多于核心数量的线程还会被回收，
     * 此值指定了线程被回收前的存活时间，默认为 2，即两分钟。
     *
     * queueSizeRejectionThreshold
     * 此属性设置队列大小拒绝阈值 - 即使未达到maxQueueSize也将发生拒绝的人为最大队列大小
     *
     * keepAliveTimeMinutes
     * 此属性设置保持活动时间，以分钟为单位。
     * maximumSize 线程池最大值 不开启拒绝HystrixCommand的情况下支持的最大并发数
     * maxQueueSize 请求等待队列  coresize<maxQueueSize<maximumSize
     * 求数小于最大线程数，却大于核心线程数的时候，会一起处理所有的请求，
     * 当所有请求处理完毕的时候，会将多余核心数量的线程释放。
     *
     * numBuckets  设置rollingstatistical窗口划分的桶数。
     * timeInMilliseconds 此属性设置滚动窗口的持续时间，其中保留执行时间以允许百分位数计算，以毫秒为单位。numBuckets
     *
     * maxQueueSize+maximumSize  = 最大并发
     * maximumSize = 最大线程池数
     * keepAliveTimeMinutes 空闲释放时间 = maxQueueSize - coreSize
     * queueSizeRejectionThreshold 立即拒绝数
     * @return
     * @throws Exception
     */
    @HystrixCommand(
            commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "5"),
                    @HystrixProperty(name = "maxQueueSize", value = "20"),
                    @HystrixProperty(name = "maximumSize", value = "30"),
                    @HystrixProperty(name = "allowMaximumSizeToDivergeFromCoreSize", value = "true"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100"),
//                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
//                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            }
    )
    @RequestMapping("/test2")
    @ResponseBody
    public String test2() throws Exception {
        log.info("-----1-----");
        Thread.sleep(1000);
        return "test2";
    }


    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
    })
    @RequestMapping("/test1")
    @ResponseBody
    public String test1() throws Exception {
        Thread.sleep(1000);
        return "test1";
    }
}
