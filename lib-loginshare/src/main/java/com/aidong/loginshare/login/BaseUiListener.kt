package com.aidong.loginshare.login

import com.aidong.loginshare.util.QQUtil
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import org.json.JSONObject

/**
 * @description 调用SDK已经封装好的接口时，例如：登录、快速支付登录、应用分享、应用邀请等接口，需传入该回调的实例。
 * @author simple.wu
 * @date 2023/2/6
 */
class BaseUiListener(private val callback: QQLoginShareCallback) : IUiListener {
    override fun onComplete(response: Any?) {
        if (response == null || (response as JSONObject).length() == 0) {
            onError(UiError(-1000, "response is empty, login fail", "response is empty, login fail"))
            return
        }
        QQUtil.initOpenidAndToken(response)
        callback.onComplete(response)
    }

    override fun onError(error: UiError?) {
        callback.onError(com.aidong.loginshare.bean.UiError(error))
    }

    override fun onCancel() {
        callback.onCancel()
    }

    override fun onWarning(code: Int) {
        callback.onWarning(code)
    }
}