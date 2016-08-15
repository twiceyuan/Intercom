package com.twiceyuan.intercom.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by twiceYuan on 1/9/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Scope @Retention(RetentionPolicy.RUNTIME)
public @interface ActivitySingleton {
}
