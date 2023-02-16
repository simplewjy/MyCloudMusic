package com.aidong.loginshare.pay

/**
 * @description 支付宝支付结果回调
 * @author simple.wu
 * @date 2023/1/16
 */
interface AliPayPayCallback {
    fun paySuccess()
    fun payFailure(errorCode: String?, errorMsg: String?)
}

interface AliPayAuthCallback{
    fun authSuccess(result: String)
    fun authFailure(resultStatus: String?, resultCode: String?)
}