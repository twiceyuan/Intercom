package com.twiceyuan.intercom.common;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by twiceYuan on 8/4/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * Firebase 操作帮助类
 */
public class FirebaseUtil {

    /**
     * 读取一个数据的快照
     *
     * @param ref       数据的引用
     * @param typeClass 数据的实体类型
     * @param <T>       数据的实体类型
     * @return 数据的一次订阅值
     */
    public static <T> Observable<T> readSnapshot(Query ref, Class<T> typeClass) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        T snapshot = dataSnapshot.getValue(typeClass);
                        subscriber.onNext(snapshot);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                });
            }
        });
    }

    /**
     * 观察一个节点值的变化
     *
     * @param ref ref
     * @param typeClass 类型
     * @param <T> 类型
     * @return 观测对象
     */
    public static <T> Observable<T> observeValue(DatabaseReference ref, Class<T> typeClass) {
        final ValueEventListener[] valueEventListener = new ValueEventListener[1];
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                valueEventListener[0] = ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subscriber.onNext(dataSnapshot.getValue(typeClass));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onCompleted();
                    }
                });
            }
        }).doOnUnsubscribe(() -> {
            if (valueEventListener[0] != null) {
                ref.removeEventListener(valueEventListener[0]);
            }
        });
    }

    public static boolean isEmptyUrl(String s) {
        return (TextUtils.isEmpty(s) || s.equals("null"));
    }

    public static <T> Observable<T> setValue(DatabaseReference ref, T t, Class<T> clazz) {
        return Observable.<Observable<T>>create(subscriber -> {
            ref.setValue(t, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    subscriber.onError(databaseError.toException());
                } else {
                    subscriber.onNext(readSnapshot(databaseReference, clazz));
                    subscriber.onCompleted();
                }
            });
        }).flatMap(RxUtil::unpack);
    }
}
