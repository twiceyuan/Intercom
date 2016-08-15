package com.twiceyuan.intercom.injector.components;


import com.twiceyuan.intercom.injector.ActivitySingleton;
import com.twiceyuan.intercom.injector.modules.ActivityModule;
import com.twiceyuan.intercom.injector.modules.GoogleClientModule;
import com.twiceyuan.intercom.ui.view.WelcomeActivity;

import dagger.Component;

/**
 * Created by twiceYuan on 1/10/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@ActivitySingleton
@Component(dependencies = {AppComponent.class}, modules = {ActivityModule.class, GoogleClientModule.class})
public interface ActivityComponent {
    void inject(WelcomeActivity activity);
}
