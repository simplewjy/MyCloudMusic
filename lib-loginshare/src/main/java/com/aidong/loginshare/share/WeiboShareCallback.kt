package com.aidong.loginshare.share

import com.sina.weibo.sdk.common.UiError

/**
 * @description
 * @author simple.wu
 * @date 2023/1/29
 */
interface WeiboShareCallback {
    fun onComplete()

    fun onError(error: UiError?)

    fun onCancel()
}