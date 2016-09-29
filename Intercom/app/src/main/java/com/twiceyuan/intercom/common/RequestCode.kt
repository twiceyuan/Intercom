package com.twiceyuan.intercom.common

import java.util.concurrent.atomic.AtomicInteger

/**
 * 获得全局唯一的 RequestCode，避免值的碰撞
 */
object RequestCode {
    private val seed = AtomicInteger()
    fun get(): Int {
        return seed.incrementAndGet()
    }
}