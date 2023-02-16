package com.aidong.loginshare.pay

/**
 * @description
 * @author simple.wu
 * @date 2023/1/11
 */
interface WXPayCallback {
    fun paySuccess()
    fun payUserCancel()
    fun payFailure(errorCode: Int, errorMsg: String)
}

interface WXAuthCallback {
    fun authSuccess(code: String)
    fun authUserCancel()
    fun authFailure(errorCode: String, errorMsg: String)
}