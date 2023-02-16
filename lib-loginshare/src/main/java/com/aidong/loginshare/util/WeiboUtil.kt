package com.aidong.loginshare.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.aidong.loginshare.bean.*
import com.aidong.loginshare.data.PublicData
import com.aidong.loginshare.login.WeiboLoginCallback
import com.aidong.loginshare.share.WeiboShareCallback
import com.sina.weibo.sdk.api.*
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.openapi.IWBAPI
import com.sina.weibo.sdk.openapi.SdkListener
import com.sina.weibo.sdk.openapi.WBAPIFactory
import com.sina.weibo.sdk.share.WbShareCallback
import java.lang.Exception
import java.util.*

/**
 * @description
 * @author simple.wu
 * @date 2023/1/29
 */
object WeiboUtil {
    private const val TAG = "WeiboUtil"
    private var mWBAPI: IWBAPI? = null

    /**
     * 初始化SDK
     */
    @JvmStatic
    fun initSDK(context: Context) {
        val authInfo = AuthInfo(context, PublicData.WEIBO_APP_KEY, PublicData.REDIRECT_URL, PublicData.SCOPE)
        mWBAPI = WBAPIFactory.createWBAPI(context)
        mWBAPI?.registerApp(context, authInfo, object : SdkListener {
            override fun onInitSuccess() {
                Log.i(TAG, "init sdk success")
            }

            override fun onInitFailure(p0: Exception?) {
                Log.i(TAG, "init sdk fail, error msg is ${p0?.message}")
            }
        })
    }

    /**
     * sso授权登录(优先选择客户端，若无客户端则选择h5登录的方式)
     */
    @Throws(UninitializedPropertyAccessException::class)
    @JvmStatic
    @JvmOverloads
    fun startAuth(activity: Activity, callBack: WeiboLoginCallback? = null) {
        if (mWBAPI == null) throw UninitializedPropertyAccessException("please call the initSDk method first")
        //auth
        mWBAPI?.authorize(activity, object : WbAuthListener {
            override fun onComplete(token: Oauth2AccessToken) {
                callBack?.authSuccess(token)
            }

            override fun onError(error: UiError) {
                callBack?.authFail(error)
            }

            override fun onCancel() {
                callBack?.authCancel()
            }
        })
    }

    /**
     * sso授权登录（客户端）
     */
    @Throws(UninitializedPropertyAccessException::class)
    @JvmStatic
    @JvmOverloads
    fun startClientAuth(activity: Activity, callBack: WeiboLoginCallback? = null) {
        if (mWBAPI == null) throw UninitializedPropertyAccessException("please call the initSDk method first")
        if (mWBAPI?.isWBAppInstalled != true) {
            Toast.makeText(activity, "请先安装微博客户端", Toast.LENGTH_SHORT).show()
        }
        mWBAPI?.authorizeClient(activity, object : WbAuthListener {
            override fun onComplete(token: Oauth2AccessToken) {
                callBack?.authSuccess(token)
            }

            override fun onError(error: UiError) {
                callBack?.authFail(error)
            }

            override fun onCancel() {
                callBack?.authCancel()
            }
        })
    }

    /**
     * sso授权登录（web）
     */
    @Throws(UninitializedPropertyAccessException::class)
    @JvmStatic
    @JvmOverloads
    fun startWebAuth(activity: Activity, callBack: WeiboLoginCallback? = null) {
        if (mWBAPI == null) throw UninitializedPropertyAccessException("please call the initSDk method first")
        mWBAPI?.authorizeWeb(activity, object : WbAuthListener {
            override fun onComplete(token: Oauth2AccessToken) {
                callBack?.authSuccess(token)
            }

            override fun onError(error: UiError) {
                callBack?.authFail(error)
            }

            override fun onCancel() {
                callBack?.authCancel()
            }
        })
    }

    /**
     * 在调用sso登录activity的onActivityResult中调用该方法
     */
    fun authorizeCallback(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        mWBAPI?.authorizeCallback(activity, requestCode, resultCode, data)
    }

    /**
     * 分享结果回调
     */
    @JvmOverloads
    fun shareResultCallback(data: Intent?, weiboShareCallback: WeiboShareCallback? = null) {
        mWBAPI?.doResultIntent(data, object : WbShareCallback {
            override fun onComplete() {
                weiboShareCallback?.onComplete()
            }

            override fun onError(error: UiError?) {
                weiboShareCallback?.onError(error)
            }

            override fun onCancel() {
                weiboShareCallback?.onCancel()
            }
        })
    }

    /**
     * weibo分享
     */
    @JvmOverloads
    fun doWeiboShare(
        activity: Activity,
        shareOnlyByClient: Boolean = true,
        shareText: Boolean = false,
        shareImg: Boolean = false,
        shareWebPage: Boolean = false,
        shareMultipleImg: Boolean = false,
        shareVideo: Boolean = false,
        shareTextBean: WBShareTextBean? = null,
        shareImgBean: WBShareImgBean? = null,
        shareWebpageBean: WBShareWebPageBean? = null,
        shareMultiImgBeanList: List<WBShareMultiImgBean>? = null,
        shareVideoBean: WBShareVideoBean? = null
    ) {
        if (mWBAPI?.isWBAppInstalled != true && shareOnlyByClient) {
            Toast.makeText(activity, "请先安装微博客户端", Toast.LENGTH_SHORT).show()
        }
        val message = WeiboMultiMessage()
        if (shareText) {
            //分享文字
            val textObject = TextObject()
            textObject.text = shareTextBean?.text ?: ""
            message.textObject = textObject
        }
        if (shareImg) {
            //分享图片
            val imageObject = ImageObject()
            imageObject.setImageData(shareImgBean?.bitmap)
            message.imageObject = imageObject
        }
        if (shareWebPage) {
            //分享网页
            val webObject = WebpageObject()
            webObject.identify = shareWebpageBean?.identify
            webObject.title = shareWebpageBean?.title
            webObject.description = shareWebpageBean?.description
            webObject.thumbData = shareWebpageBean?.getThumbData()
            webObject.actionUrl = shareWebpageBean?.actionUrl
            webObject.defaultText = shareWebpageBean?.defaultText
            message.mediaObject = webObject
        }
        if (shareMultipleImg) {
            if (mWBAPI?.isWBAppSupportMultipleImage == true) {
                // 分享多图
                val multiImageObject = MultiImageObject()
                val uriList = arrayListOf<Uri>()
                shareMultiImgBeanList?.forEach {
                    uriList.add(it.uri)
                }
                multiImageObject.imageList = uriList
                message.multiImageObject = multiImageObject
            } else {
                Toast.makeText(activity, "请安装最新版微博以支持多图分享", Toast.LENGTH_SHORT).show()
            }
        }
        if (shareVideo) {
            //分享视频
            val videoObject = VideoSourceObject()
            videoObject.videoPath = shareVideoBean?.videoPath
            message.videoSourceObject = videoObject
        }
        mWBAPI?.shareMessage(activity, message, shareOnlyByClient)
    }
}