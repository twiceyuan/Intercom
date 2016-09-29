package com.twiceyuan.intercom.common

/**
 * Created by twiceYuan on 8/7/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
object CommonUtil {

    fun equals(a: Any?, b: Any?): Boolean {
        return if (a == null) b == null else a == b
    }
}
