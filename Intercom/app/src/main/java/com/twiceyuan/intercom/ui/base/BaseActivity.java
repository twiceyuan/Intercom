package com.twiceyuan.intercom.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.Toaster;
import com.twiceyuan.intercom.injector.components.ActivityComponent;
import com.twiceyuan.intercom.injector.components.DaggerActivityComponent;
import com.twiceyuan.intercom.injector.components.DaggerUserActivityComponent;
import com.twiceyuan.intercom.injector.components.UserComponent;
import com.twiceyuan.intercom.injector.modules.ActivityModule;
import com.twiceyuan.intercom.injector.modules.GoogleClientModule;
import com.twiceyuan.log.L;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 万物基于 BaseActivity
 */
public class BaseActivity extends AppCompatActivity {

    private CompositeSubscription mSubscriptions;
    private ActivityComponent     mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 异步任务管理
        mSubscriptions = new CompositeSubscription();
        // 注入对象
        onInject(getActivityComponent());
        // 约束获取 Intent 数据的位置
        initIntentData(getIntent());
    }

    public void initIntentData(Intent intent) {
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        // 如果实现这个空接口，就设置返回按钮
        if (this instanceof CanBack) {
            enableBackNavigationButton();
        }
    }

    public void enableBackNavigationButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected <T extends View> T bind(@IdRes int id) {
        //noinspection unchecked
        return (T) findViewById(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    public <T> void runRx(Observable<T> observable, Action1<T> subscriber, Action1<Throwable> errorHandler) {
        mSubscriptions.add(observable.subscribe(subscriber, errorHandler));
    }

    public <T> void runRx(Observable<T> observable, Action1<T> subscriber) {
        mSubscriptions.add(observable.subscribe(subscriber, this::onThrow));
    }

    public <T> void runApi(Observable<T> observable, Action1<T> subscriber, Action1<Throwable> errorHandler) {
        runRx(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread()), subscriber, errorHandler);
    }

    public <T> void runApi(Observable<T> observable, Action1<T> subscriber) {
        runRx(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread()), subscriber, this::onThrow);
    }

    public void onThrow(Throwable throwable) {
        Toaster.s(throwable.getMessage());
    }

    public <T> void runRx(Observable<T> observable, Action1<T> subscriber, Action1<Throwable> errorHandler, Action0 completeHandler) {
        mSubscriptions.add(observable.subscribe(subscriber, errorHandler, completeHandler));
    }

    public ActivityComponent getActivityComponent() {

        if (mActivityComponent == null) {
            UserComponent userComponent = App.get().getUserComponent();
            if (userComponent != null) {
                mActivityComponent = DaggerUserActivityComponent.builder()
                        .userComponent(userComponent)
                        .activityModule(new ActivityModule(this))
                        .googleClientModule(new GoogleClientModule(this))
                        .build();
            } else {
                mActivityComponent = DaggerActivityComponent.builder()
                        .appComponent(App.get().getAppComponent())
                        .activityModule(new ActivityModule(this))
                        .googleClientModule(new GoogleClientModule(this))
                        .build();
            }
        }
        return mActivityComponent;
    }

    public void onInject(ActivityComponent component) {
        // child to inject
    }

    public void loadImage(String url, Action1<Drawable> callback) {
        Glide.with(this).load(url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                callback.call(resource);
            }
        });
    }

    public void requestCodeNotFound() {
        L.i("no match request code.");
    }

    public Activity getContext() {
        return this;
    }
}
