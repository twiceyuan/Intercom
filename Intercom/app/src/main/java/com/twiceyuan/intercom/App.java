package com.twiceyuan.intercom;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;
import com.twiceyuan.intercom.injector.components.AppComponent;
import com.twiceyuan.intercom.injector.components.DaggerAppComponent;
import com.twiceyuan.intercom.injector.components.DaggerUserComponent;
import com.twiceyuan.intercom.injector.components.UserComponent;
import com.twiceyuan.intercom.injector.modules.AppModule;
import com.twiceyuan.intercom.injector.modules.UserModule;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 全局 Application
 */
public class App extends Application {

    private static App sGlobalApplication;

    private AppComponent mAppComponent;
    private UserComponent mUserComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sGlobalApplication = this;

        getAppComponent().inject(this);
    }

    public AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return mAppComponent;
    }

    public static App get() {
        return sGlobalApplication;
    }

    public UserComponent createUserComponent(FirebaseUser user) {
        mUserComponent = DaggerUserComponent.builder().appComponent(getAppComponent())
                .userModule(new UserModule(user))
                .build();
        return mUserComponent;
    }

    public UserComponent getUserComponent() {
        return mUserComponent;
    }

    public void releaseUserComponent() {
        mUserComponent = null;
    }
}
