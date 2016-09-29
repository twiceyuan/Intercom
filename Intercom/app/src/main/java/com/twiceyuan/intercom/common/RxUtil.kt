package com.twiceyuan.intercom.common

import rx.Observable

/**
 * Created by twiceYuan on 8/6/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
object RxUtil {

    fun <T> unpack(observable: Observable<T>): Observable<T> {
        return observable
    }
}
