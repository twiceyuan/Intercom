package com.twiceyuan.intercom.common;

import rx.Observable;

/**
 * Created by twiceYuan on 8/6/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class RxUtil {

    public static <T> Observable<T> unpack(Observable<T> observable) {
        return observable;
    }
}
