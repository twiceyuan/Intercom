package com.twiceyuan.intercom.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.common.Logger;
import com.twiceyuan.intercom.common.Toaster;
import com.twiceyuan.intercom.config.Constants;
import com.twiceyuan.intercom.injector.components.ActivityComponent;
import com.twiceyuan.intercom.model.local.User;
import com.twiceyuan.intercom.ui.base.BaseActivity;
import com.twiceyuan.intercom.ui.view.account.EmailLoginActivity;
import com.twiceyuan.intercom.ui.view.social.ConversationsActivity;

import javax.inject.Inject;


/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 欢迎界面，提供第三方登录方法
 */
public class WelcomeActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    @Inject FirebaseAuth      mAuth;
    @Inject GoogleApiClient   mGoogleApiClient;
    @Inject DatabaseReference mDatabaseReference;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                DatabaseReference remoteUser = mDatabaseReference.child(Constants.DB_PROFILE).child(user.getUid());
                FirebaseUtil.readSnapshot(remoteUser, User.class).subscribe(userSnapshot -> {
                    if (userSnapshot == null) {
                        remoteUser.setValue(new User.Builder().from(user).build());
                    }
                });

                /**
                 * 登录成功，注入 user 对象供相关组件使用
                 */
                App.get().createUserComponent(user);

                Toaster.s(String.format(getString(R.string.user_logon), user.getEmail()));

                ConversationsActivity.start(this);

                finish();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void loginByGoogle(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toaster.s(getString(R.string.login_failed));
            }
        }

        if (requestCode == Constants.REQUEST_EMAIL_LOGIN) {
            EmailLoginActivity.handleResult(resultCode, data,
                    (email, password) -> mAuth.signInWithEmailAndPassword(email, password),
                    (email, password) -> mAuth.createUserWithEmailAndPassword(email, password)
            );
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Logger.d("firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Logger.d("signInWithCredential:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Logger.e("signInWithCredential", task.getException());
                        Toaster.s(getString(R.string.auth_failed));
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toaster.s(String.format(getString(R.string.login_failed_arg), connectionResult.getErrorMessage()));
    }

    public void loginByEmail(View view) {
        EmailLoginActivity.start(this);
    }

    @Override
    public void onInject(ActivityComponent component) {
        component.inject(this);
    }
}
