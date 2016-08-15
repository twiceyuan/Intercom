package com.twiceyuan.intercom.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 获得全局唯一的 RequestCode，避免值的碰撞
 */
public class RequestCode {
    private static final AtomicInteger seed = new AtomicInteger();
    public static int get() {
        return seed.incrementAndGet();
    }
}