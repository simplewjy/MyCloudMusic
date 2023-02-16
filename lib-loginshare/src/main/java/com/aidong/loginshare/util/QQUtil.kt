package com.aidong.loginshare.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.aidong.loginshare.R
import com.aidong.loginshare.bean.*
import com.aidong.loginshare.data.PublicData
import com.aidong.loginshare.login.BaseUiListener
import com.aidong.loginshare.login.QQLoginMode
import com.aidong.loginshare.login.QQLoginShareCallback
import com.aidong.loginshare.share.QQShareBehavior
import com.aidong.loginshare.share.QZoneShareBehavior
import com.tencent.connect.auth.AuthAgent
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzonePublish
import com.tencent.connect.share.QzoneShare
import com.tencent.open.apireq.BaseResp
import com.tencent.open.apireq.IApiCallback
import com.tencent.tauth.Tencent
import org.json.JSONObject


/**
 * @description qq工具类
 * @author simple.wu
 * @date 2023/2/6
 */
object QQUtil {
    private var mTencent: Tencent? = null
    private var mAuthLoginListener: BaseUiListener? = null
    private var mShareListener: BaseUiListener? = null
    private const val TAG = "QQUtil"

    /**
     * 初始化SDK
     */
    fun initSDK(context: Context) {
        val authorities = "${context.packageName}.fileprovider"
        mTencent = Tencent.createInstance(PublicData.QQ_APP_ID, context.applicationContext, authorities)
    }

    @JvmStatic
    fun setIsPermissionGranted(isChecked: Boolean) {
        Tencent.setIsPermissionGranted(isChecked)
    }

    /**
     * qq授权登录
     * @param mode 授权登陆的形式
     * @param showWebDownloadLink 是否显示扫码页面下载qq的链接地址
     */
    @JvmStatic
    @JvmOverloads
    fun authLogin(
        activity: Activity,
        callback: QQLoginShareCallback,
        mode: QQLoginMode,
        showWebDownloadLink: Boolean = false
    ) {
        if (mTencent == null) {
            Log.d(TAG, "authLogin: init sdk first")
            return
        }
        if (isSessionValid() == true) {
            //说明授权暂未过期，需要先退出登录
            logout(activity)
        }
        activity.intent.putExtra(AuthAgent.KEY_FORCE_QR_LOGIN, mode == QQLoginMode.QRCODE)
        val params = HashMap<String, Any>()
        if (activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            params[Constants.KEY_RESTORE_LANDSCAPE] = true
        }
        params[Constants.KEY_SCOPE] = "all"
        params[Constants.KEY_QRCODE] = (mode == QQLoginMode.QRCODE || mode == QQLoginMode.CLIENT_QRCODE)
        params[Constants.KEY_ENABLE_SHOW_DOWNLOAD_URL] = showWebDownloadLink

        mAuthLoginListener = BaseUiListener(callback)
        mTencent?.login(activity, mAuthLoginListener, params)
    }

    /**
     * 授权是否过期
     */
    @JvmStatic
    fun isSessionValid(): Boolean? {
        return mTencent?.isSessionValid
    }


    /**
     * 退出登录
     */
    @JvmStatic
    fun logout(activity: Activity) {
        if (mTencent == null) {
            Log.d(TAG, "logout: init sdk first")
            return
        }
        mTencent?.logout(activity)
    }

    /**
     * 跳转至qq登录授权管理页
     */
    fun gotoAuthPage(activity: Activity) {
        mTencent?.startAuthManagePage(activity, object : IApiCallback {
            override fun onResp(baseResp: BaseResp?) {
                if (baseResp?.isSuccess == true) {
                    // 跳转成功
                    return
                }
                val showMsg = when (baseResp?.code) {
                    BaseResp.CODE_QQ_NOT_INSTALLED -> activity.getString(R.string.qq_not_install)
                    BaseResp.CODE_UNSUPPORTED_BRANCH -> activity.getString(R.string.qq_branch_not_support)
                    BaseResp.CODE_QQ_LOW_VERSION -> activity.getString(R.string.upgrade_qq)
                    BaseResp.CODE_NOT_LOGIN -> activity.getString(R.string.need_login)
                    else -> baseResp.toString()
                }
                Toast.makeText(activity, showMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @JvmStatic
    fun initOpenidAndToken(jsonObject: JSONObject) {
        try {
            val token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
            val expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
            val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)
            ) {
                mTencent?.setAccessToken(token, expires)
                mTencent?.openId = openId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 接收登录成功后返回的数据在调用login的Activity或者Fragment重写onActivityResult方法,调用该方法
     */
    fun resultCallback(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mAuthLoginListener)
        }
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener)
        }
    }


    /**
     * 分享到qq捏
     */
    @JvmStatic
    fun doShareToQQ(activity: Activity, qqShareBean: QQShareBaseBean, qqShareCallback: QQLoginShareCallback) {
        Handler(Looper.getMainLooper()).post {
            // QQ分享要在主线程做
            if (mTencent == null) {
                Log.d(TAG, "authLogin: init sdk first")
                return@post
            }
            mShareListener = BaseUiListener(qqShareCallback)
            val params = Bundle()
            when (qqShareBean.behavior) {
                QQShareBehavior.IMAGE_TEXT -> {
                    val bean = qqShareBean as QQShareImageTextBean
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, bean.shareToQQTitle)
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, bean.shareToQQSummary)
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, bean.shareToQQTargetUrl)
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, bean.shareToQQImgUrl)
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, bean.shareToQQAppName)
                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, bean.shareToQQExtInt)
                }
                QQShareBehavior.IMAGE -> {
                    val bean = qqShareBean as QQShareImageBean
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, bean.shareToQQImgUrl)
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, bean.shareToQQAppName)
                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, bean.shareToQQExtInt)
                }
                QQShareBehavior.MUSIC_VIDEO -> {
                    val bean = qqShareBean as QQShareMusicVideoBean
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO)
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, bean.shareToQQTitle)
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, bean.shareToQQSummary)
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, bean.shareToQQTargetUrl)
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, bean.shareToQQImgUrl)
                    params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, bean.shareToQQAudioUrl)
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, bean.shareToQQAppName)
                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, bean.shareToQQExtInt)
                }
                QQShareBehavior.MINI_PROGRAM -> {
                    val bean = qqShareBean as QQShareMiniProgramBean
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_MINI_PROGRAM)
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, bean.shareToQQTitle)
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, bean.shareToQQSummary)
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, bean.shareToQQTargetUrl)
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, bean.shareToQQImgUrl)
                    params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID, bean.shareToQQMiniProgramAppId)
                    params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH, bean.shareToQQMiniProgramPath)
                    params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE, bean.shareToQQMiniProgramType)
                }
                else -> {

                }
            }
            mTencent?.shareToQQ(activity, params, mShareListener)
        }
    }

    /**
     * 分享到qq空间
     */
    @JvmStatic
    fun doShareToQZone(activity: Activity, qqShareBean: QQShareBaseBean, qqShareCallback: QQLoginShareCallback) {
        // QZone分享要在主线程做
        Handler(Looper.getMainLooper()).post {
            if (mTencent == null) {
                Log.d(TAG, "authLogin: init sdk first")
                return@post
            }
            mShareListener = BaseUiListener(qqShareCallback)
            val params = Bundle()

            when (qqShareBean.qZoneBehavior) {
                QZoneShareBehavior.IMAGE_TEXT -> {
                    val bean = qqShareBean as QZoneShareImageTextBean
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
                    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, bean.shareToQZoneTitle)//必填
                    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, bean.shareToQZoneSummary)//选填
                    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, bean.shareToQZoneTargetUrl)//必填
                    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, bean.shareToQZoneImgList)
                }
                QZoneShareBehavior.PUBLISH_MOOD -> {
                    val bean = qqShareBean as QZoneSharePublishMoodBean
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD)
                    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, bean.shareToQZoneSummary)
                    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, bean.shareToQZoneImgList)
                    val extParams = Bundle()
                    extParams.putString(QzonePublish.HULIAN_EXTRA_SCENE, bean.shareToQZoneExtraScene)
                    extParams.putString(QzonePublish.HULIAN_CALL_BACK, bean.shareToQZoneCallback)
                    params.putBundle(QzonePublish.PUBLISH_TO_QZONE_EXTMAP, extParams)
                }
                QZoneShareBehavior.PUBLISH_VIDEO -> {
                    val bean = qqShareBean as QZoneSharePublicVideoBean
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO)
                    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, bean.shareToQZoneSummary)
                    params.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, bean.shareToQZoneVideoPath)
                    val extParams = Bundle()
                    extParams.putString(QzonePublish.HULIAN_EXTRA_SCENE, bean.shareToQZoneExtraScene)
                    extParams.putString(QzonePublish.HULIAN_CALL_BACK, bean.shareToQZoneCallback)
                    params.putBundle(QzonePublish.PUBLISH_TO_QZONE_EXTMAP, extParams)
                }
                QZoneShareBehavior.MINI_PROGRAM -> {
                    val bean = qqShareBean as QZoneShareMiniProgramBean
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_MINI_PROGRAM);
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, bean.shareToQZoneTitle)
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, bean.shareToQZoneSummary)
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, bean.shareToQZoneTargetUrl)
                    params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, bean.shareToQZoneImgList)
                    params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID, bean.shareToQZoneMiniProgramAppId)
                    params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH, bean.shareToQZoneMiniProgramPath)
                    params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE, bean.shareToQZoneMiniProgramType)
                }
                else -> {

                }
            }

            mTencent?.shareToQzone(activity, params, mShareListener)
        }
    }


//    /**
//    //     * 更新用户信息
//    //     */
//    private fun updateUserInfo(activity: Activity) {
//        if (mTencent != null && mTencent?.isSessionValid == true) {
//            val listener: IUiListener = object : DefaultUiListener() {
//                override fun onError(e: UiError) {}
//                override fun onComplete(response: Any) {
//                    val msg = Message()
//                    msg.obj = response
//                    msg.what = 0
//                    mHandler.sendMessage(msg)
//                    object : Thread() {
//                        override fun run() {
//                            val json = response as JSONObject
//                            if (json.has("figureurl")) {
//                                var bitmap: Bitmap? = null
//                                try {
//                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"))
//                                } catch (e: JSONException) {
//                                    SLog.e(MainActivity.TAG, "Util.getBitmap() jsonException : " + e.message)
//                                }
//                                val msg = Message()
//                                msg.obj = bitmap
//                                msg.what = 1
//                                mHandler.sendMessage(msg)
//                            }
//                        }
//                    }.start()
//                }
//
//                override fun onCancel() {}
//            }
//            val info = UserInfo(this, MainActivity.mTencent.getQQToken())
//            info.getUserInfo(listener)
//        } else {
//            mUserInfo.setText("")
//            mUserInfo.setVisibility(View.GONE)
//            mUserLogo.setVisibility(View.GONE)
//        }
//    }
}