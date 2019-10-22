package com.pikaqiu.concurrency.example.rateLimiter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimiterExample2 {

    private static RateLimiter rateLimiter = RateLimiter.create(5);

    public static void main(String[] args) throws Exception {

        for (int index = 0; index < 100; index++) {
            double acquire = rateLimiter.acquire();
            handle(index, acquire);
        }
    }


    private static void handle(int i,double waitTime) {
        log.info("{}，waitTime: {}", i, waitTime);
    }


}
