package com.twiceyuan.intercom.injector.components;

import com.twiceyuan.intercom.injector.ActivitySingleton;
import com.twiceyuan.intercom.injector.modules.FragmentModule;

import dagger.Component;

/**
 * Created by twiceYuan on 1/10/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@ActivitySingleton
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

}
