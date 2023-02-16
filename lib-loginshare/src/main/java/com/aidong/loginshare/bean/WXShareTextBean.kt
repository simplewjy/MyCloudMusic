package com.aidong.loginshare.bean

import com.aidong.loginshare.share.WXShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/1/9
 */
data class WXShareTextBean(
    val textString: String
): WXShareBaseBean() {
    init {
        behavior = WXShareBehavior.TEXT
    }
}