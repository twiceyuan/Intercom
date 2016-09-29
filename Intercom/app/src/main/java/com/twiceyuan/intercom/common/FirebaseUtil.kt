package com.twiceyuan.intercom.common

import android.text.TextUtils
import com.google.firebase.database.*
import rx.Observable

/**
 * Created by twiceYuan on 8/4/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 *
 * Firebase 操作帮助类
 */
object FirebaseUtil {

    /**
     * 读取一个数据的快照
     *
     * @param ref       数据的引用
     * @param typeClass 数据的实体类型
     * @return 数据的一次订阅值
     */
    fun <T> readSnapshot(ref: Query, typeClass: Class<T>): Observable<T> {
        return Observable.create { subscriber ->
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val snapshot = dataSnapshot.getValue(typeClass)
                    subscriber.onNext(snapshot)
                    subscriber.onCompleted()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    subscriber.onError(databaseError.toException())
                }
            })
        }
    }

    /**
     * 观察一个节点值的变化
     *
     * @param ref ref
     * @param typeClass 类型
     * @return 观测对象
     */
    fun <T> observeValue(ref: DatabaseReference, typeClass: Class<T>): Observable<T> {
        val valueEventListener = arrayOfNulls<ValueEventListener>(1)
        return Observable.create(Observable.OnSubscribe<T> { subscriber ->
            valueEventListener[0] = ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    subscriber.onNext(dataSnapshot.getValue(typeClass))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    subscriber.onCompleted()
                }
            })
        }).doOnUnsubscribe {
            if (valueEventListener[0] != null) {
                ref.removeEventListener(valueEventListener[0])
            }
        }
    }

    fun isEmptyUrl(s: String): Boolean {
        return TextUtils.isEmpty(s) || s == "null"
    }

    fun <T> setValue(ref: DatabaseReference, t: T): Observable<T> {
        return Observable.create<T> { subscriber ->
            ref.setValue(t, { databaseError: DatabaseError?, databaseReference: DatabaseReference? ->
                if (databaseError != null) {
                    subscriber.onError(databaseError.toException())
                } else {
                    subscriber.onNext(t)
                    subscriber.onCompleted()
                }
            })
        }
    }
}
