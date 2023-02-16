package com.aidong.loginshare.login

import com.tencent.tauth.UiError
import org.json.JSONObject

/**
 * @description
 * @author simple.wu
 * @date 2023/2/6
 */
interface QQLoginShareCallback {
    /**
     * 分享或登录成功
     */
    fun onComplete(response: JSONObject)

    /**
     * 分享或登录异常
     */
    fun onError(error: com.aidong.loginshare.bean.UiError?)

    /**
     * 取消分享或登录
     */
    fun onCancel()

    /**
     * 分享或登录警告，如无权限等
     */
    fun onWarning(code: Int)
}