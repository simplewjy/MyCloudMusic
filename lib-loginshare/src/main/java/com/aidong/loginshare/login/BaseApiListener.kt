package com.aidong.loginshare.login

import com.tencent.open.utils.HttpUtils
import com.tencent.tauth.IRequestListener
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.SocketTimeoutException

/**
 * @description  使用requestAsync、request等通用方法调用sdk未封装的接口时，例如上传图片、查看相册等，需传入该回调的实例。
 * @author simple.wu
 * @date 2023/2/6
 */
class BaseApiListener : IRequestListener {
    override fun onComplete(p0: JSONObject?) {

    }

    override fun onIOException(p0: IOException?) {

    }

    override fun onMalformedURLException(p0: MalformedURLException?) {

    }

    override fun onJSONException(p0: JSONException?) {

    }

    override fun onSocketTimeoutException(p0: SocketTimeoutException?) {

    }

    override fun onNetworkUnavailableException(p0: HttpUtils.NetworkUnavailableException?) {

    }

    override fun onHttpStatusException(p0: HttpUtils.HttpStatusException?) {

    }

    override fun onUnknowException(p0: Exception?) {

    }
}