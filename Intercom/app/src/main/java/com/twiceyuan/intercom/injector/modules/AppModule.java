package com.twiceyuan.intercom.injector.modules;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.injector.AppContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by twiceYuan on 1/9/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Module
public class AppModule {

    private final App mApp;

    public AppModule(App app) {
        mApp = app;
    }

    @Provides
    App provideApp() {
        return mApp;
    }

    @Provides
    @AppContext
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public FirebaseAuth provideAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public DatabaseReference provideDatabaseRef() {
        return FirebaseDatabase.getInstance().getReference().getRoot();
    }
}