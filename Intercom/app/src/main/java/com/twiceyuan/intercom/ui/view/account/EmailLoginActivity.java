package com.twiceyuan.intercom.ui.view.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Window;

import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.RequestCode;
import com.twiceyuan.intercom.common.Toaster;
import com.twiceyuan.intercom.config.Constants;
import com.twiceyuan.intercom.ui.base.BaseActivity;
import com.twiceyuan.intercom.ui.common.TabPagerAdapter;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 注册界面，用户名和密码+邮箱（找回密码）
 */
public class EmailLoginActivity extends BaseActivity {

    public static final int REQUEST_EMAIL_LOGIN = RequestCode.INSTANCE.get();

    private static final String EXTRA_EMAIL    = "email";
    private static final String EXTRA_PASSWORD = "password";
    private static final String EXTRA_TYPE     = "type";

    private static final int TYPE_LOGIN    = 0;
    private static final int TYPE_REGISTER = 1;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static void start(Activity context) {
        Intent starter = new Intent(context, EmailLoginActivity.class);
        context.startActivityForResult(starter, EmailLoginActivity.REQUEST_EMAIL_LOGIN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_email_login);
        setFinishOnTouchOutside(false);

        viewPager = bind(R.id.viewPager);
        tabLayout = bind(R.id.tabLayout);

        setupViewPager();
    }

    private void setupViewPager() {
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());

        final InputAccountFragment loginFragment = InputAccountFragment.newInstance();
        final InputAccountFragment registerFragment = InputAccountFragment.newInstance();

        adapter.addFragment(loginFragment, getString(R.string.login));
        adapter.addFragment(registerFragment, getString(R.string.register));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    registerFragment.getAccountInfo(loginFragment::setAccountInfo);
                    loginFragment.moveToLast();
                }
                if (tab.getPosition() == 1) {
                    loginFragment.getAccountInfo(registerFragment::setAccountInfo);
                    registerFragment.moveToLast();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        loginFragment.setOnSubmitListener((email, password) -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_EMAIL, email);
            intent.putExtra(EXTRA_PASSWORD, password);
            intent.putExtra(EXTRA_TYPE, TYPE_LOGIN);
            setResult(RESULT_OK, intent);
            finish();
        });

        registerFragment.setOnSubmitListener((email, password) -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_EMAIL, email);
            intent.putExtra(EXTRA_PASSWORD, password);
            intent.putExtra(EXTRA_TYPE, TYPE_REGISTER);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    public static void handleResult(int resultCode, Intent data,
                                    @NonNull InputAccountFragment.InfoCallback loginCallback,
                                    @NonNull InputAccountFragment.InfoCallback registerCallback) {
        if (resultCode != RESULT_OK) {
            Toaster.INSTANCE.s("操作取消");
            return;
        }
        String email = data.getStringExtra(EXTRA_EMAIL);
        String password = data.getStringExtra(EXTRA_PASSWORD);
        int type = data.getIntExtra(EXTRA_TYPE, -1);
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toaster.INSTANCE.s("信息不完整");
            return;
        }
        switch (type) {
            case TYPE_LOGIN:
                loginCallback.call(email, password);
                break;
            case TYPE_REGISTER:
                registerCallback.call(email, password);
                break;
            default:
                Toaster.INSTANCE.s("信息不合法");
        }
    }
}
