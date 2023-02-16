package com.aidong.loginshare.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aidong.loginshare.R
import com.aidong.loginshare.pay.WXAuthCallback
import com.aidong.loginshare.pay.WXPayCallback
import com.aidong.loginshare.util.WXUtil
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessView
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessWebview
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * @description WX登录、分享以及支付功能页面
 * @author simple.wu
 * @date 2023/1/6
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {

    companion object {
        private const val TAG = "WXEntryActivity"
        var wxPayCallback: WXPayCallback? = null
        var wxAuthCallback: WXAuthCallback? = null
    }

    override fun onReq(req: BaseReq?) {}

    override fun onResp(resp: BaseResp?) {
        if (resp?.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.i(TAG, "onPayFinish,errCode=" + resp.errCode)
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    wxPayCallback?.paySuccess()
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    wxPayCallback?.payUserCancel()
                }
                else -> {
                    wxPayCallback?.payFailure(resp.errCode, resp.errStr)
                }
            }
            finish()
        }

        if (resp?.type == ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE) {
            val subscribeMsgResp = resp as SubscribeMessage.Resp
            val text = String.format(
                "openid=%s\ntemplate_id=%s\nscene=%d\naction=%s\nreserved=%s",
                subscribeMsgResp.openId, subscribeMsgResp.templateID, subscribeMsgResp.scene, subscribeMsgResp.action, subscribeMsgResp.reserved
            )
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        if (resp?.type == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            val launchMiniProgramResp = resp as WXLaunchMiniProgram.Resp
            val text = String.format(
                "openid=%s\nextMsg=%s\nerrStr=%s",
                launchMiniProgramResp.openId, launchMiniProgramResp.extMsg, launchMiniProgramResp.errStr
            )
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        if (resp?.type == ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW) {
            val launchMiniProgramResp = resp as WXOpenBusinessView.Resp
            val text = String.format(
                "openid=%s\nextMsg=%s\nerrStr=%s\nbusinessType=%s",
                launchMiniProgramResp.openId, launchMiniProgramResp.extMsg, launchMiniProgramResp.errStr, launchMiniProgramResp.businessType
            )
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        if (resp?.type == ConstantsAPI.COMMAND_OPEN_BUSINESS_WEBVIEW) {
            val response = resp as WXOpenBusinessWebview.Resp
            val text = String.format("businessType=%d\nresultInfo=%s\nret=%d", response.businessType, response.resultInfo, response.errCode)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        if (resp?.type == ConstantsAPI.COMMAND_SENDAUTH) {
            val authResp = resp as SendAuth.Resp
            val code = authResp.code

            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    wxAuthCallback?.authSuccess(code)
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    wxAuthCallback?.authUserCancel()
                }
                else -> {
                    wxAuthCallback?.authFailure(resp.errCode.toString(), resp.errStr)
                }
            }
            finish()
//            NetworkUtil.sendWxAPI(
//                handler, String.format(
//                    "https://api.weixin.qq.com/sns/oauth2/access_token?" +
//                            "appid=%s&secret=%s&code=%s&grant_type=authorization_code", "wxd930ea5d5a258f4f",
//                    "1d6d1d57a3dd063b36d917bc0b44d964", code
//                ), NetworkUtil.GET_TOKEN
//            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        WXUtil.api?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        WXUtil.api?.handleIntent(intent, this)
    }
}