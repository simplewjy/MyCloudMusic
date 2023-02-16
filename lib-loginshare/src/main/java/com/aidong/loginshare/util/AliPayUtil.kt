package com.aidong.loginshare.util

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.aidong.loginshare.bean.AuthResult
import com.aidong.loginshare.bean.PayResult
import com.aidong.loginshare.data.PublicData
import com.aidong.loginshare.pay.AliPayAuthCallback
import com.aidong.loginshare.pay.AliPayPayCallback
import com.alipay.sdk.app.AuthTask
import com.alipay.sdk.app.PayTask

/**
 * @description
 * @author simple.wu
 * @date 2023/1/16
 */
object AliPayUtil {
    private const val PAY_SUCCESS_CODE = "9000"
    private const val LOGIN_SUCCESS_CODE = "200"

    /**
     * 支付宝支付
     */
    fun pay(activity: Activity, aliPayCallback: AliPayPayCallback? = null) {
        val payRunnable = Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(PublicData.ALI_PAY_ORDER_INFO, true)
            Log.i("msp", result.toString())
            val payResult = PayResult(result)

            // 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
            val resultInfo: String? = payResult.result // 同步返回需要验证的信息
            val resultStatus: String? = payResult.resultStatus
            // 判断resultStatus 为9000则代表支付成功
            if (resultStatus == PAY_SUCCESS_CODE) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Handler(Looper.getMainLooper()).post {
                    aliPayCallback?.paySuccess()
                }
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Handler(Looper.getMainLooper()).post {
                    aliPayCallback?.payFailure(resultStatus, payResult.result)
                }
            }
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 支付宝授权
     */
    fun auth(activity: Activity, aliPayCallback: AliPayAuthCallback? = null) {
        val authRunnable = Runnable { // 构造AuthTask 对象
            val authTask = AuthTask(activity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(PublicData.ALI_PAY_AUTH_INFO, true)
            val authResult = AuthResult(result, true)
            val resultStatus: String = authResult.resultStatus
            // 判断resultStatus 为“9000”且result_code
            // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
            if (resultStatus == PAY_SUCCESS_CODE && authResult.resultCode == "200") {
                // 获取alipay_open_id，调支付时作为参数extern_token 的value
                // 传入，则支付账户为该授权账户
                Handler(Looper.getMainLooper()).post {
                    aliPayCallback?.authSuccess(authResult.result)
                }
            } else {
                // 其他状态值则为授权失败
                Handler(Looper.getMainLooper()).post {
                    aliPayCallback?.authFailure(resultStatus, authResult.resultCode)
                }
            }
        }
        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }
}