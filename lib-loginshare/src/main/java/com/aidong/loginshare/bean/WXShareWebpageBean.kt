package com.aidong.loginshare.bean

import com.aidong.loginshare.share.WXShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/1/10
 */
class WXShareWebpageBean(
    var pageUrl: String? = null,
    var coverUrl: String? = null,
    var pageTitle: String = "",
    var pageDesc: String = ""
) : WXShareBaseBean() {
    init {
        behavior = WXShareBehavior.WEBPAGE
    }
}