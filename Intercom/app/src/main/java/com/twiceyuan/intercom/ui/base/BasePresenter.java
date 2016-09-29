package com.twiceyuan.intercom.ui.base;

import com.twiceyuan.intercom.common.Toaster;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * MVP 的 Presenter 部分的基类
 */
public abstract class BasePresenter<View extends BaseView> {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    private View mImplView;

    public void attachView(View view) {
        mImplView = view;
    }

    public void detachView() {
        mImplView = null;
        mSubscriptions.unsubscribe();
    }

    public View getImplView() {
        return mImplView;
    }

    public <T> void runRx(Observable<T> observable, Action1<T> subscriber, Action1<Throwable> errorHandler) {
        mSubscriptions.add(observable.subscribe(subscriber, errorHandler));
    }

    public <T> void runRx(Observable<T> observable, Action1<T> subscriber) {
        mSubscriptions.add(observable.subscribe(subscriber, this::onThrow));
    }

    public void onThrow(Throwable throwable) {
        Toaster.INSTANCE.s(throwable.getMessage());
    }

    public <T> void runRx(Observable<T> observable, Action1<T> subscriber, Action1<Throwable> errorHandler, Action0 completeHandler) {
        mSubscriptions.add(observable.subscribe(subscriber, errorHandler, completeHandler));
    }
}
