package com.twiceyuan.intercom.common

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by twiceYuan on 8/13/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
object TimeUtil {

    /**
     * 转换日期 转换为更为人性化的时间

     * @param time 给定时间 13位，包含毫秒
     */
    fun translateDate(time: Long): String {
        var timeVar = time
        timeVar /= 1000
        val curTime = System.currentTimeMillis() / 1000
        val oneDay = 24 * 60 * 60.toLong()
        val today = Calendar.getInstance()    //今天
        today.timeInMillis = curTime * 1000
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        val todayStartTime = today.timeInMillis / 1000
        if (timeVar >= todayStartTime) {
            val d = curTime - timeVar
            if (d <= 60) {
                return "1分钟前"
            } else if (d <= 60 * 60) {
                var m = d / 60
                if (m <= 0) {
                    m = 1
                }
                return "$m 分钟前"
            } else {
                val dateFormat = SimpleDateFormat("今天 HH:mm", Locale.getDefault())
                val date = Date(timeVar * 1000)
                var dateStr = dateFormat.format(date)
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ")
                }
                return dateStr
            }
        } else {
            if (timeVar < todayStartTime && timeVar > todayStartTime - oneDay) {
                val dateFormat = SimpleDateFormat("昨天 HH:mm", Locale.getDefault())
                val date = Date(timeVar * 1000)
                var dateStr = dateFormat.format(date)
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {

                    dateStr = dateStr.replace(" 0", " ")
                }
                return dateStr
            } else if (timeVar < todayStartTime - oneDay && timeVar > todayStartTime - 2 * oneDay) {
                val dateFormat = SimpleDateFormat("前天 HH:mm", Locale.getDefault())
                val date = Date(timeVar * 1000)
                var dateStr = dateFormat.format(date)
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ")
                }
                return dateStr
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val date = Date(timeVar * 1000)
                var dateStr = dateFormat.format(date)
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ")
                }
                return dateStr
            }
        }
    }
}
