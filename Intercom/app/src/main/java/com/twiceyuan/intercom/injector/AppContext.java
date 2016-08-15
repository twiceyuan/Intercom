package com.twiceyuan.intercom.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by twiceYuan on 1/14/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface AppContext {
}
