package com.aidong.loginshare.bean

import android.graphics.Bitmap
import com.aidong.loginshare.share.WXShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/1/9
 */
data class WXShareImgBean(
    var bitMap: Bitmap? = null, var url: String? = null
) : WXShareBaseBean() {
    init {
        behavior = WXShareBehavior.IMAGE
    }
}