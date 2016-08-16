package com.twiceyuan.intercom.ui.base;

import android.content.Intent;

import com.twiceyuan.intercom.injector.components.ActivityComponent;
import com.twiceyuan.intercom.injector.components.UserActivityComponent;
import com.twiceyuan.intercom.ui.view.WelcomeActivity;

/**
 * Created by twiceYuan on 8/8/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 有登录用户的 BaseActivity 主要负责 FirebaseUser 对象的注入
 */
public class BaseUserActivity extends BaseActivity {

    @Override
    public final void onInject(ActivityComponent component) {
        if (component instanceof UserActivityComponent) {
            onInject((UserActivityComponent) component);
        } else {
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    public void onInject(UserActivityComponent component) {

    }
}
