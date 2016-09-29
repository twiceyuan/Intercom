package com.twiceyuan.intercom.injector.modules;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.Toaster;
import com.twiceyuan.intercom.injector.ActivitySingleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by twiceYuan on 8/5/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Module
public class GoogleClientModule {

    private FragmentActivity mActivity;

    public GoogleClientModule(FragmentActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivitySingleton
    GoogleApiClient getClient() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, this::connectResult)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void connectResult(ConnectionResult result) {
        Toaster.INSTANCE.s(mActivity.getString(R.string.google_connect_failed));
    }
}
