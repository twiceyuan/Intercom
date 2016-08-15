package com.twiceyuan.intercom.common;

import android.text.TextUtils;
import android.util.Log;

import com.twiceyuan.intercom.App;

/**
 * Created by twiceYuan on 8/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class Toaster {

    private static android.widget.Toast mToast;

    public static void s(String message) {
        c(message, android.widget.Toast.LENGTH_SHORT);
    }

    public static void s(String message, boolean log) {
        s(message);
        if (log) {
            Logger.i(message);
        }
    }

    public static void l(String message) {
        c(message, android.widget.Toast.LENGTH_LONG);
    }

    public static void c(String message, int duration) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(App.get(), message, duration);
            mToast.show();
        } else {
            mToast.setText(message);
            mToast.setDuration(duration);
            mToast.show();
        }
    }

    public static void e(Throwable throwable) {
        if (throwable != null) {
            Log.e("Toast", throwable.getMessage(), throwable);
            if (TextUtils.isEmpty(throwable.getMessage())) {
                s("发生了错误");
            } else {
                String message = (throwable.getMessage());
                s(message);
                Log.e("Error", throwable.getMessage(), throwable);
            }
        } else {
            s("发生了错误");
        }
    }
}
