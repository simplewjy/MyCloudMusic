package com.aidong.loginshare.bean

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID

/**
 * @description
 * @author simple.wu
 * @date 2023/1/29
 */
data class WBShareWebPageBean(
    val identify: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "描述",
    val bitmap: Bitmap,
    val actionUrl: String = "https://weibo.com",
    val defaultText: String = "分享网页"
) {
    fun getThumbData(): ByteArray? {
        var os: ByteArrayOutputStream? = null
        return try {
            os = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os)
            os.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}