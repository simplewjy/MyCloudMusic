package com.aidong.loginshare.util

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.util.Log
import com.aidong.loginshare.data.PublicData
import com.aidong.loginshare.login.OneKeyLoginCallback
import com.mobile.auth.gatewayauth.AuthUIConfig
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet

/**
 * @description
 * @author simple.wu
 * @date 2023/1/12
 */
object OneKeyLoginUtil {
    private const val TAG = "OneKeyLoginUtil"

    private var mPhoneNumberAuthHelper: PhoneNumberAuthHelper? = null

    private var mTokenResultListener: TokenResultListener? = null

    @JvmStatic
    @JvmOverloads
    fun initSDK(context: Context, callback: OneKeyLoginCallback? = null) {
        mTokenResultListener = object : TokenResultListener {
            override fun onTokenSuccess(s: String) {
                Log.e(TAG, "获取token成功：$s")
                try {
                    val tokenRet: TokenRet = TokenRet.fromJson(s)
                    if (ResultCode.CODE_START_AUTHPAGE_SUCCESS == tokenRet.code) {
                        callback?.startAuthPageSuccess()
                        Log.i("TAG", "唤起授权页成功：$s")
                    }
                    if (ResultCode.CODE_SUCCESS == tokenRet.code) {
                        Log.i("TAG", "获取token成功：$s")
                        callback?.getTokenSuccess(tokenRet.token)
                        mPhoneNumberAuthHelper?.setAuthListener(null)
                        mPhoneNumberAuthHelper?.quitLoginPage()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTokenFailed(s: String) {
                Log.e(TAG, "获取token失败：$s")
                mPhoneNumberAuthHelper?.hideLoginLoading()
                try {
                    val tokenRet = TokenRet.fromJson(s)
                    if (ResultCode.CODE_ERROR_USER_CANCEL == tokenRet?.code) {
                        Log.d(TAG, "user cancel")
                        callback?.userCancelAuth()
                    } else if (ResultCode.CODE_ERROR_USER_SWITCH == tokenRet?.code) {
                        Log.d(TAG, "user change other channel")
                        callback?.userChangeOtherLoginChannel()
                    } else {
                        callback?.onFailure(tokenRet?.code, tokenRet?.msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mPhoneNumberAuthHelper?.quitLoginPage()
                mPhoneNumberAuthHelper?.setAuthListener(null)
            }
        }
        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener)
        mPhoneNumberAuthHelper?.reporter?.setLoggerEnable(true)
        mPhoneNumberAuthHelper?.setAuthSDKInfo(PublicData.ALI_ONE_KEY_LOGIN_SECRET)
    }

    /**
     * 拉起授权页
     * @param timeout 超时时间
     */
    fun getLoginToken(context: Context, timeout: Int) {
        mPhoneNumberAuthHelper?.getLoginToken(context, timeout)
    }

    @JvmStatic
    @JvmOverloads
    fun oneKeyLogin(context: Context, authUIConfig: AuthUIConfig? = null) {

        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener)
        mPhoneNumberAuthHelper?.checkEnvAvailable(PhoneNumberAuthHelper.SERVICE_TYPE_LOGIN)
//        mUIConfig.configAuthPage()
        //用户控制返回键及左上角返回按钮效果
//        mPhoneNumberAuthHelper?.userControlAuthPageCancel()
        //用户禁用utdid
        //mPhoneNumberAuthHelper.prohibitUseUtdid();
        //授权页是否跟随系统深色模式
        mPhoneNumberAuthHelper?.setAuthPageUseDayLight(true)
        //授权页物理返回键禁用
        //mPhoneNumberAuthHelper.closeAuthPageReturnBack(true);
        //横屏水滴屏全屏适配
        //mPhoneNumberAuthHelper.keepAuthPageLandscapeFullSreen(true);
        //授权页扩大协议按钮选择范围至我已阅读并同意
        //mPhoneNumberAuthHelper.expandAuthPageCheckedScope(true);
        //授权页物理返回键禁用
        //mPhoneNumberAuthHelper.closeAuthPageReturnBack(true);
        if (authUIConfig != null) {
            mPhoneNumberAuthHelper?.setAuthUIConfig(authUIConfig)
        } else {
            mPhoneNumberAuthHelper?.setAuthUIConfig(getDefaultAuthUIConfig(context))
        }
        getLoginToken(context, 5000)
    }

    private fun getDefaultAuthUIConfig(context: Context): AuthUIConfig {
        return AuthUIConfig.Builder()
//            .setAppPrivacyOne(
//                "隐私政策",
//                "http://coach.aidong.me/privacy/privacy.html"
//            )
//            .setAppPrivacyColor(Color.GRAY, Color.BLUE)
//            .setNavReturnHidden(true)
            .setPrivacyState(false)
            .setStatusBarColor(Color.TRANSPARENT)
            .setNavHidden(false)
            .setLightColor(true)
            .setSloganHidden(false)
            .setSloganText("中国移动提供认证服务")
//            .setLogBtnBackgroundPath("bg_red_solid_2")
//            .setSwitchAccText("其他号码登录")
//            .setSwitchAccTextColor(Color.RED)
//            .setNumberColor(Color.RED)
//            .setNumberSizeDp(25)
            .setAuthPageActIn("slide_in_right", "slide_out_left")
            .setAuthPageActOut("slide_in_left", "slide_out_right")
            .setVendorPrivacyPrefix("《")
            .setVendorPrivacySuffix("》")
//            .setLogoImgDrawable(
//                ContextCompat.getDrawable(
//                    context, R.drawable.send_img
//                )
//            )
//            .setLogoOffsetY(60)
//            .setLogoWidth(210)
//            .setLogoHeight(30)
//            .setCheckedImgDrawable(
//                ContextCompat.getDrawable(
//                    context, R.drawable.send_music_thumb
//                )
//            )
            .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
            .create()
    }
}