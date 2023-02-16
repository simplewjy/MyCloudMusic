package com.aidong.loginshare.login

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.common.UiError

/**
 * @description 微博sso登录回调
 * @author simple.wu
 * @date 2023/1/29
 */
interface WeiboLoginCallback {
    fun authSuccess(token: Oauth2AccessToken)
    fun authFail(error: UiError)
    fun authCancel()
}