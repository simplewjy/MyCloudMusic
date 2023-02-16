package com.aidong.loginshare.bean

import com.aidong.loginshare.share.QZoneShareBehavior
import com.tencent.connect.share.QQShare

/**
 * @description
 * @author simple.wu
 * @date 2023/2/9
 */
data class QZoneShareImageTextBean(
    val shareToQZoneTitle: String = "",
    val shareToQZoneSummary: String = "",
    val shareToQZoneTargetUrl: String = "",
    val shareToQZoneImgList: ArrayList<String> = arrayListOf()
): QQShareBaseBean() {
    init {
        qZoneBehavior = QZoneShareBehavior.IMAGE_TEXT
    }
}
