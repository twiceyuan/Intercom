package com.twiceyuan.intercom.injector.modules;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.twiceyuan.intercom.injector.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by twiceYuan on 4/17/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    Fragment provideFragment() {
        return mFragment;
    }

    @Provides
    @ActivityContext
    public Context provideActivityContext() {
        return mFragment.getActivity();
    }
}
