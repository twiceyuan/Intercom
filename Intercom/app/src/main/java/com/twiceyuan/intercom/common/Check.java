package com.twiceyuan.intercom.common;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by twiceYuan on 8/4/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 检查回调
 */
public class Check {

    public static <T> void notNull(T t, Action1<T> callback) {
        if (t != null && callback != null)
            callback.call(t);
    }

    public static void ifNull(Object o, Action0 callback) {
        if (o == null && callback != null) {
            callback.call();
        }
    }
}
