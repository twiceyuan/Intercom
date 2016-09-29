package com.twiceyuan.intercom.common

import android.text.TextUtils

import com.twiceyuan.intercom.App
import com.twiceyuan.log.L

/**
 * Created by twiceYuan on 8/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
object Toaster {

    private var mToast: android.widget.Toast? = null

    fun s(message: String) {
        c(message, android.widget.Toast.LENGTH_SHORT)
    }

    fun s(message: String, log: Boolean) {
        s(message)
        if (log) {
            L.i(message)
        }
    }

    fun l(message: String) {
        c(message, android.widget.Toast.LENGTH_LONG)
    }

    fun c(message: String, duration: Int) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(App.get(), message, duration)
            mToast!!.show()
        } else {
            mToast!!.setText(message)
            mToast!!.duration = duration
            mToast!!.show()
        }
    }

    fun e(throwable: Throwable?) {
        if (throwable != null) {
            L.e(throwable)
            if (TextUtils.isEmpty(throwable.message)) {
                s("发生了错误")
            } else {
                val message : String?= throwable.message
                if (message != null) {
                    s(message)
                }
            }
        } else {
            s("发生了错误，但没有错误信息")
        }
    }
}
