package com.aidong.loginshare.bean

import com.aidong.loginshare.share.QZoneShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/2/9
 */
data class QZoneSharePublicVideoBean(
    val shareToQZoneSummary: String = "",
    val shareToQZoneVideoPath: String = "",
    val shareToQZoneExtraScene: String = "",
    val shareToQZoneCallback: String = ""
): QQShareBaseBean() {
    init {
        qZoneBehavior = QZoneShareBehavior.PUBLISH_VIDEO
    }
}

