package com.twiceyuan.intercom.injector.modules;

import com.google.firebase.auth.FirebaseUser;
import com.twiceyuan.intercom.injector.UserScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by twiceYuan on 8/8/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Module
public class UserModule {

    private FirebaseUser mLogonUser;

    public UserModule(FirebaseUser logonUser) {
        mLogonUser = logonUser;
    }

    @Provides @UserScope
    public FirebaseUser provideLogonUser() {
        return mLogonUser;
    }
}
