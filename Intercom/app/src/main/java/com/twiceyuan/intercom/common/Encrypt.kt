package com.twiceyuan.intercom.common

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by twiceYuan on 8/9/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
object Encrypt {

    fun md5(origin: String): String? {
        try {
            return byteArrayToHex(MessageDigest.getInstance("MD5").digest(origin.toByteArray(charset("UTF-8"))))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

    }

    fun byteArrayToHex(byteArray: ByteArray): String {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val resultCharArray = CharArray(byteArray.size * 2)
        var index = 0
        for (b in byteArray) {
            // Kotlin 中位运算仅限于 Int 和 Long 因此需要先显式转换为 Int 型
            resultCharArray[index++] = hexDigits[b.toInt().ushr(4) and 0xf]
            resultCharArray[index++] = hexDigits[b.toInt() and 0xf]
        }
        return String(resultCharArray)
    }
}
