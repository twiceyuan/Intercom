package com.twiceyuan.intercom.common

import rx.functions.Action0
import rx.functions.Action1

/**
 * Created by twiceYuan on 8/4/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 *
 * 检查回调
 */
object Check {

    fun <T> notNull(t: T?, callback: Action1<T>?) {
        if (t != null && callback != null)
            callback.call(t)
    }

    fun ifNull(o: Any?, callback: Action0?) {
        if (o == null && callback != null) {
            callback.call()
        }
    }
}
