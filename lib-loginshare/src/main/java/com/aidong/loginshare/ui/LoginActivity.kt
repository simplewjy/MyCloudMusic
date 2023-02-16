package com.aidong.loginshare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.aidong.loginshare.R
import com.aidong.loginshare.bean.*
import com.aidong.loginshare.pay.WXAuthCallback
import com.aidong.loginshare.pay.WXPayCallback
import com.aidong.loginshare.share.WXShareScene
import com.aidong.loginshare.util.WXUtil

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initListener()
    }

    private fun initListener() {
        findViewById<Button>(R.id.btn_login).setOnClickListener {
            WXUtil.login(this, object : WXAuthCallback {
                override fun authSuccess(code: String) {
                    Toast.makeText(this@LoginActivity, "auth success code is $code", Toast.LENGTH_SHORT).show()
                }

                override fun authUserCancel() {
                    Toast.makeText(this@LoginActivity, "用户取消", Toast.LENGTH_SHORT).show()
                }

                override fun authFailure(errorCode: String, errorMsg: String) {
                    Toast.makeText(this@LoginActivity, "auth fail msg is $errorMsg", Toast.LENGTH_SHORT).show()
                }

            })
        }
        findViewById<Button>(R.id.btn_send_text).setOnClickListener {
            val bean = WXShareTextBean("这是一段测试文本").apply {
                scene = WXShareScene.SESSION
            }
            WXUtil.share(this, bean)
        }
        findViewById<Button>(R.id.btn_share_text).setOnClickListener {
            val bean = WXShareTextBean("这是一段测试文本").apply {
                scene = WXShareScene.TIMELINE
            }
            WXUtil.share(this, bean)
        }
        findViewById<Button>(R.id.btn_send_img).setOnClickListener {
            val bean = WXShareImgBean(url = "https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF").apply {
                scene = WXShareScene.SESSION
            }
            WXUtil.share(this, bean)
        }
        findViewById<Button>(R.id.btn_share_img).setOnClickListener {
            val bean = WXShareImgBean(url = "https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF").apply {
                scene = WXShareScene.TIMELINE
            }
            WXUtil.share(this, bean)
        }


        findViewById<Button>(R.id.btn_send_video).setOnClickListener {
            val bean = WXShareVideoBean(
                videoTitle = "测试发送视频给朋友的功能",
                videoDesc = "这是一个不知道哪里搞来的分享视频",
                coverUrl = "https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF",
                videoUrl = "https://flv.bn.netease.com/63197fd6b65ca6f946ad9f6e3f7786a61fc6dd153c829af68737d20b5817f388940891ff3f3f1c26f9d2b1f82e331917fb1838db42d6d859eb20ee37b55d67fe8759a9de55260a5d9c527b05a50ec384c200796593f01e8ba1045b4df018e95e5f5474473d1f83e12d268c35ea07a1a2a100d784f14d1dac.mp4"
            ).apply {
                scene = WXShareScene.SESSION
            }
            WXUtil.share(this, bean)
        }

        findViewById<Button>(R.id.btn_share_video).setOnClickListener {
            val bean = WXShareVideoBean(
                videoTitle = "测试分享朋友圈功能",
                videoDesc = "这是一个不知道哪里搞来的分享视频",
                coverUrl = "https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF",
                videoUrl = "https://flv.bn.netease.com/63197fd6b65ca6f946ad9f6e3f7786a61fc6dd153c829af68737d20b5817f388940891ff3f3f1c26f9d2b1f82e331917fb1838db42d6d859eb20ee37b55d67fe8759a9de55260a5d9c527b05a50ec384c200796593f01e8ba1045b4df018e95e5f5474473d1f83e12d268c35ea07a1a2a100d784f14d1dac.mp4"
            ).apply {
                scene = WXShareScene.TIMELINE
            }
            WXUtil.share(this, bean)
        }


        findViewById<Button>(R.id.btn_send_webpage).setOnClickListener {
            val bean = WXShareWebpageBean(
                pageTitle = "测试发送网页给朋友的功能",
                pageDesc = "拜读拜读拜读",
                coverUrl = "https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF",
                pageUrl = "https://www.baidu.com/"
            ).apply {
                scene = WXShareScene.SESSION
            }
            WXUtil.share(this, bean)
        }

        findViewById<Button>(R.id.btn_share_webpage).setOnClickListener {
            val bean = WXShareWebpageBean(
                pageTitle = "测试分享网页至朋友圈的功能",
                pageDesc = "百度百度百度",
                coverUrl = "https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF",
                pageUrl = "https://www.baidu.com/"
            ).apply {
                scene = WXShareScene.TIMELINE
            }
            WXUtil.share(this, bean)
        }

        val wxPayBean = WXPayBean()
        findViewById<Button>(R.id.btn_wx_pay).setOnClickListener {
            WXUtil.pay(this, wxPayBean, object : WXPayCallback {
                override fun paySuccess() {
                    Toast.makeText(this@LoginActivity, "支付成功", Toast.LENGTH_SHORT).show()
                }

                override fun payUserCancel() {
                    Toast.makeText(this@LoginActivity, "用户取消", Toast.LENGTH_SHORT).show()
                }

                override fun payFailure(errorCode: Int, errorMsg: String) {
                    Toast.makeText(this@LoginActivity, "error msg is $errorMsg", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}