package com.twiceyuan.intercom.common

import android.content.Context
import android.util.DisplayMetrics

import com.twiceyuan.intercom.App

object DP {

    fun dp2px(dpValue: Float): Int {
        val scale = App.get().resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(pxValue: Float): Int {
        val scale = App.get().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获取屏幕宽度
     */
    fun screenWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun screenHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }
}
