package com.aidong.loginshare.bean

import com.aidong.loginshare.share.QZoneShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/2/9
 */
data class QZoneSharePublishMoodBean(
    val shareToQZoneSummary: String = "",
    val shareToQZoneImgList: ArrayList<String> = arrayListOf(),
    val shareToQZoneExtraScene: String = "",
    val shareToQZoneCallback: String = ""
): QQShareBaseBean() {
    init {
        qZoneBehavior = QZoneShareBehavior.PUBLISH_MOOD
    }
}
