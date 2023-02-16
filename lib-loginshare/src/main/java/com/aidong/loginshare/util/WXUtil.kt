package com.aidong.loginshare.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import com.aidong.loginshare.share.WXShareBehavior
import com.aidong.loginshare.bean.*
import com.aidong.loginshare.data.PublicData
import com.aidong.loginshare.pay.WXAuthCallback
import com.aidong.loginshare.pay.WXPayCallback
import com.aidong.loginshare.wxapi.WXEntryActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
 * @description
 * @author simple.wu
 * @date 2023/1/6
 */
object WXUtil {

    var api: IWXAPI? = null

    /**
     * 注册应用到微信
     */
    @JvmStatic
    private fun registerApp(context: Context) {
        // 通过 WXAPIFactory 工厂，获取 IWXAPI 的实例
        api = WXAPIFactory.createWXAPI(context, PublicData.WX_APP_ID, true)
        // 将应用的 appId 注册到微信
        api?.registerApp(PublicData.WX_APP_ID)
        //建议动态监听微信启动广播进行注册到微信
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // 将该 app 注册到微信
                api?.registerApp(PublicData.WX_APP_ID)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }

    /**
     * 微信登录逻辑
     */
    @JvmStatic
    @JvmOverloads
    fun login(context: Context, wxAuthCallback: WXAuthCallback? = null) {
        if (api == null) {
            registerApp(context.applicationContext)
        }
        val req = SendAuth.Req()
        WXEntryActivity.wxAuthCallback = wxAuthCallback
        req.scope = "snsapi_userinfo"
        req.state = "wechat_sdk_demo_test"
        api?.sendReq(req)
    }

    /**
     * 微信分享功能
     */
    @JvmStatic
    fun share(context: Context, wxShareBean: WXShareBaseBean) {
        if (api == null) {
            registerApp(context.applicationContext)
        }
        if (api?.isWXAppInstalled != true) {
            Toast.makeText(context, "未安装微信，请先安装微信", Toast.LENGTH_SHORT).show()
            return
        }
        val scene = WXShareUtil.getScene(wxShareBean.scene)
        if (scene == -1) return
        when (wxShareBean.behavior) {
            WXShareBehavior.TEXT -> {
                WXShareUtil.shareText(scene, (wxShareBean as WXShareTextBean).textString)
            }
            WXShareBehavior.IMAGE -> {
                WXShareUtil.shareImg(context, wxShareBean as WXShareImgBean)
            }
            WXShareBehavior.VIDEO -> {
                WXShareUtil.shareVideo(context, wxShareBean as WXShareVideoBean)
            }
            WXShareBehavior.WEBPAGE -> {
                WXShareUtil.shareWebPage(context, wxShareBean as WXShareWebpageBean)
            }
            WXShareBehavior.MINI_PROGRAM -> {
                WXShareUtil.shareMiniProgram()
            }
            WXShareBehavior.MUSIC_VIDEO -> {
                WXShareUtil.shareMusicVideo()
            }
            else -> {
                //do nothing
            }
        }
    }


    /**
     * 微信支付功能
     */
    @JvmStatic
    fun pay(context: Context, wxPayBean: WXPayBean, wxPayCallback: WXPayCallback) {
        if (api == null) {
            registerApp(context.applicationContext)
        }
        WXEntryActivity.wxPayCallback = wxPayCallback
        val request = PayReq()
        request.appId = wxPayBean.appId
        request.partnerId = wxPayBean.partnerId
        request.prepayId = wxPayBean.prepayId
        request.packageValue = wxPayBean.packageValue
        request.nonceStr = wxPayBean.nonceStr
        request.timeStamp = wxPayBean.timeStamp
        request.sign = wxPayBean.sign
        api?.sendReq(request)
    }
}