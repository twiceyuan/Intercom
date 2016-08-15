package com.twiceyuan.intercom.injector.modules;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.twiceyuan.intercom.injector.ActivityContext;
import com.twiceyuan.intercom.injector.ActivitySingleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by twiceYuan on 1/9/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    public Context provideActivityContext() {
        return mActivity;
    }

    @Provides
    @ActivitySingleton
    public FirebaseAnalytics provideAnalytics(@ActivityContext Context context) {
        return FirebaseAnalytics.getInstance(context);
    }
}
