package com.twiceyuan.intercom.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 带 Presenter 的 Activity 基类
 */
public class PresenterActivity<Presenter extends BasePresenter> extends BaseActivity {

    @Inject Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this instanceof BaseView) {
            // 这里如果未实现对应接口会有编译错误，不用担心
            //noinspection unchecked
            mPresenter.attachView((BaseView) this);
        }
    }

    public Presenter getPresenter() {
        return mPresenter;
    }
}
