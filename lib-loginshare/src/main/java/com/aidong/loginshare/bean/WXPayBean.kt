package com.aidong.loginshare.bean

import com.aidong.loginshare.data.PublicData

/**
 * @description 需要后台接口返回对应的参数
 * @author simple.wu
 * @date 2023/1/11
 */
data class WXPayBean(
    var appId: String = PublicData.WX_APP_ID,
    var partnerId: String = "1900000109",
    var prepayId: String = "1101000000140415649af9fc314aa427",
    var packageValue: String = "Sign=WXPay",
    var nonceStr: String = "1101000000140429eb40476f8896f4c9",
    var timeStamp: String = "1398746574",
    var sign: String = "7FFECB600D7157C5AA49810D2D8F28BC2811827B"
)