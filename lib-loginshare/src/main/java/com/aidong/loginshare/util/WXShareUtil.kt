package com.aidong.loginshare.util

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.Toast
import com.aidong.loginshare.bean.WXShareImgBean
import com.aidong.loginshare.bean.WXShareVideoBean
import com.aidong.loginshare.bean.WXShareWebpageBean
import com.aidong.loginshare.share.WXShareScene
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tencent.mm.opensdk.modelmsg.*

/**
 * @description
 * @author simple.wu
 * @date 2023/1/10
 */
object WXShareUtil {
    private const val THUMB_SIZE = 150

    /**
     * 文字分享
     */
    @JvmStatic
    fun shareText(targetScene: Int, text: String) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        val textObj = WXTextObject()
        textObj.text = text
        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = text
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = targetScene
        //调用 api 接口，发送数据到微信
        WXUtil.api?.sendReq(req)
    }

    @JvmStatic
    fun shareImg(context: Context, wxShareImgBean: WXShareImgBean) {
        when {
            wxShareImgBean.bitMap != null -> {
                shareImg(getScene(wxShareImgBean.scene), wxShareImgBean.bitMap!!)
            }
            !TextUtils.isEmpty(wxShareImgBean.url) -> {
                Glide.with(context).asBitmap().load(wxShareImgBean.url).addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        Toast.makeText(context, "图片加载失败，无法分享至微信", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let {
                            shareImg(getScene(wxShareImgBean.scene), it)
                        }
                        return false
                    }
                }).submit()
            }
            else -> {
                //do nothing
            }
        }
    }

    /**
     * 图片分享
     */
    @JvmStatic
    private fun shareImg(targetScene: Int, bitmap: Bitmap) {
//        val bmp: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.send_img)
        //初始化 WXImageObject 和 WXMediaMessage 对象
        val imgObj = WXImageObject(bitmap)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        //设置缩略图
        val thumbBmp: Bitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true)
        bitmap.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = targetScene
//        req.userOpenId = getOpenId()
        //调用 api 接口，发送数据到微信
        WXUtil.api?.sendReq(req)
    }

    @JvmStatic
    fun shareVideo(context: Context, wxShareVideoBean: WXShareVideoBean) {
        when {
            !TextUtils.isEmpty(wxShareVideoBean.coverUrl) -> {
                Glide.with(context).asBitmap().load(wxShareVideoBean.coverUrl).addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        Toast.makeText(context, "视频封面图异常，无法分享", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let { bitmap ->
                            wxShareVideoBean.videoUrl?.let {
                                shareVideo(getScene(wxShareVideoBean.scene), it, bitmap, wxShareVideoBean.videoTitle, wxShareVideoBean.videoDesc)
                            }
                        }
                        return false
                    }
                }).submit()
            }
            else -> {
                //do nothing
            }
        }
    }

    /**
     * 视频分享
     */
    @JvmStatic
    @JvmOverloads
    fun shareVideo(targetScene: Int, videoUrl: String, videoCoverBitmap: Bitmap, videoTitle: String = "", videoDesc: String = "") {
        //初始化一个WXVideoObject，填写url
        val video = WXVideoObject()
        video.videoUrl = videoUrl
        //用 WXVideoObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(video)
        msg.title = videoTitle
        msg.description = videoDesc
//        val thumbBmp = BitmapFactory.decodeResource(context.resources, R.drawable.send_music_thumb)
        val thumbBmp: Bitmap = Bitmap.createScaledBitmap(videoCoverBitmap, THUMB_SIZE, THUMB_SIZE, true)
        videoCoverBitmap.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("video")
        req.message = msg
        req.scene = targetScene
//        req.userOpenId = getOpenId()
        //调用 api 接口，发送数据到微信
        WXUtil.api?.sendReq(req)
    }


    fun shareWebPage(context: Context, wxShareWebpageBean: WXShareWebpageBean) {
        when {
            !TextUtils.isEmpty(wxShareWebpageBean.coverUrl) -> {
                Glide.with(context).asBitmap().load(wxShareWebpageBean.coverUrl).addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        Toast.makeText(context, "网页封面图异常，无法分享", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let { bitmap ->
                            wxShareWebpageBean.pageUrl?.let {
                                shareWebPage(getScene(wxShareWebpageBean.scene), it, bitmap, wxShareWebpageBean.pageTitle, wxShareWebpageBean.pageDesc)
                            }
                        }
                        return false
                    }
                }).submit()
            }
            else -> {
                //do nothing
            }
        }
    }

    /**
     * 网页分享
     */
    private fun shareWebPage(targetScene: Int, webpageUrl: String, pageCoverBitmap: Bitmap, pageTitle: String = "", pageDesc: String = "") {
        //初始化一个WXWebpageObject，填写url
        val webpage = WXWebpageObject()
        webpage.webpageUrl = webpageUrl
        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(webpage)
        msg.title = pageTitle
        msg.description = pageDesc
        val thumbBmp: Bitmap = Bitmap.createScaledBitmap(pageCoverBitmap, THUMB_SIZE, THUMB_SIZE, true)
        pageCoverBitmap.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = targetScene
//        req.userOpenId = getOpenId()
        //调用 api 接口，发送数据到微信
        WXUtil.api?.sendReq(req)
    }

    /**
     * 分享小程序
     */
    fun shareMiniProgram() {
        val miniProgramObj = WXMiniProgramObject()
        miniProgramObj.webpageUrl = "http://www.qq.com" // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE // 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = "gh_d43f693ca31f" // 小程序原始id
        miniProgramObj.path = "/pages/media" //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        val msg = WXMediaMessage(miniProgramObj)
        msg.title = "小程序消息Title" // 小程序消息title
        msg.description = "小程序消息Desc" // 小程序消息desc
        //todo 获取小程序封面图片
//        msg.thumbData = getThumb() // 小程序消息封面图片，小于128k
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("miniProgram")
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession // 目前只支持会话
        WXUtil.api?.sendReq(req)
    }

    /**
     * 分享音乐
     */
    fun shareMusicVideo() {
        val musicVideo = WXMusicVideoObject()
        musicVideo.musicUrl = "https://www.qq.com" // 音乐url
        musicVideo.musicDataUrl = "http://xxx/xx.mp3" // 音乐音频url
        musicVideo.songLyric = "xxx" // 歌词
        musicVideo.hdAlbumThumbFilePath = "xxx" // 专辑图本地文件路径
        musicVideo.singerName = "xxx"
        musicVideo.albumName = "album_xxx"
        musicVideo.musicGenre = "流行歌曲"
        musicVideo.issueDate = 1610713585
        musicVideo.identification = "sample_identification"
        musicVideo.duration = 120000 // 单位为毫秒
        val msg = WXMediaMessage()
        msg.mediaObject = musicVideo
        msg.title = "歌曲名称" // 必填，不能为空
        msg.description = "歌曲描述" // 选填，建议与歌手名字段 singerName 保持一致
        msg.messageExt = "额外信息" // 微信跳回应用时会带上
//        msg.thumbData = getThumb() // 音乐卡片缩略图，不超过64KB
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("musicVideo")
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession // 支持会话、朋友圈、收藏
        WXUtil.api?.sendReq(req)
    }

    fun getScene(scene: WXShareScene?): Int {
        return when (scene) {
            WXShareScene.SESSION -> SendMessageToWX.Req.WXSceneSession
            WXShareScene.TIMELINE -> SendMessageToWX.Req.WXSceneTimeline
            WXShareScene.FAVORITE -> SendMessageToWX.Req.WXSceneTimeline
            else -> -1
        }
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }
}