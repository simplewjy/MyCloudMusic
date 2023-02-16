package com.aidong.loginshare.bean

import com.aidong.loginshare.share.WXShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/1/10
 */
data class WXShareVideoBean(
    var videoUrl: String? = null,
    var coverUrl: String? = null,
    var videoTitle: String = "",
    var videoDesc: String = ""
) : WXShareBaseBean() {
    init {
        behavior = WXShareBehavior.VIDEO
    }
}
