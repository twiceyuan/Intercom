package com.twiceyuan.intercom.common;

/**
 * Created by twiceYuan on 8/17/15.
 * <p>
 * Log 的简化方法集
 * 用途：省略 TAG，默认使用调用该方法的代码所在的类名作为 TAG
 */
public final class Logger {

    private Logger() {
    }

    public static void i(String message) {
        android.util.Log.i(getCallerClassName(), message);
    }

    public static void w(String message) {
        android.util.Log.w(getCallerClassName(), message);
    }

    public static void e(String message) {
        android.util.Log.e(getCallerClassName(), message);
    }

    public static void v(String message) {
        android.util.Log.v(getCallerClassName(), message);
    }

    public static void wtf(String message) {
        android.util.Log.wtf(getCallerClassName(), message);
    }

    public static void i(String message, Throwable tr) {
        android.util.Log.i(getCallerClassName(), message, tr);
    }

    public static void w(String message, Throwable tr) {
        android.util.Log.w(getCallerClassName(), message, tr);
    }

    public static void e(String message, Throwable tr) {
        android.util.Log.e(getCallerClassName(), message, tr);
    }

    public static void v(String message, Throwable tr) {
        android.util.Log.v(getCallerClassName(), message, tr);
    }

    public static void wtf(String message, Throwable tr) {
        android.util.Log.wtf(getCallerClassName(), message, tr);
    }

    public static void d(String message) {
        android.util.Log.d(getCallerClassName(), message);
    }

    public static void d(String mesaage, Throwable throwable) {
        android.util.Log.d(getCallerClassName(), mesaage, throwable);
    }

    /**
     * 获得调用该方法的类
     *
     * @return 调用该方法的类
     */
    private static String getCallerClassName() {
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement[] entries = e.getStackTrace();

            /**
             * entries[0] 是本方法
             * entries[1] 是本类中的四个静态方法
             * entries[2] 是直接调用本类中静态方法的类名
             */
            String fullClassName = entries[2].getClassName();
            int lastPointIndex = fullClassName.lastIndexOf(".");
            if (lastPointIndex > -1) {
                return fullClassName.substring(lastPointIndex + 1);
            } else {
                return fullClassName;
            }
        }
    }

    public static void e(Throwable throwable) {
        android.util.Log.e(getCallerClassName(), throwable.getMessage(), throwable);
    }

    public static void i(String prepare, Object... args) {
        android.util.Log.i(getCallerClassName(), String.format(prepare, args));
    }

    public static void e(String prepare, Object... args) {
        android.util.Log.e(getCallerClassName(), String.format(prepare, args));
    }

    public static void w(String prepare, Object... args) {
        android.util.Log.w(getCallerClassName(), String.format(prepare, args));
    }

    public static void v(String prepare, Object... args) {
        android.util.Log.v(getCallerClassName(), String.format(prepare, args));
    }

    public static void wtf(String prepare, Object... args) {
        android.util.Log.wtf(getCallerClassName(), String.format(prepare, args));
    }

    public static void d(String prepare, Object... args) {
        android.util.Log.d(getCallerClassName(), String.format(prepare, args));
    }
}
