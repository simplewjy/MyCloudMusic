package com.aidong.loginshare.bean

import com.aidong.loginshare.share.QQShareBehavior
import com.tencent.connect.share.QQShare

/**
 * @description
 * @author simple.wu
 * @date 2023/2/8
 */
data class QQShareImageBean(
    val shareToQQImgUrl: String = "",
    val shareToQQAppName: String = "",
    val shareToQQExtInt: Int = QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE
): QQShareBaseBean() {
    init {
        behavior = QQShareBehavior.IMAGE
    }
}