package com.aidong.loginshare.login


/**
 * @description
 * @author simple.wu
 * @date 2023/1/13
 */
interface OneKeyLoginCallback {
    /**
     * 获取token成功
     */
    fun getTokenSuccess(token: String)

    /**
     * 启动授权页成功
     */
    fun startAuthPageSuccess()

    /**
     * 用户取消
     */
    fun userCancelAuth()

    /**
     * 用户点击使用其他方式登录
     */
    fun userChangeOtherLoginChannel()

    /**
     * 授权失败
     */
    fun onFailure(errorCode: String?, errorMsg: String?)
}