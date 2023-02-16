package com.aidong.loginshare.bean

import com.aidong.loginshare.share.QQShareBehavior
import com.aidong.loginshare.share.QZoneShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/2/9
 */
data class QZoneShareMiniProgramBean(
    val shareToQZoneTitle: String = "",
    val shareToQZoneSummary: String = "",
    val shareToQZoneTargetUrl: String = "",
    val shareToQZoneImgList: ArrayList<String> = arrayListOf(),
    val shareToQZoneMiniProgramAppId: String = "",
    val shareToQZoneMiniProgramPath: String = "",
    val shareToQZoneMiniProgramType: String = ""
): QQShareBaseBean() {
    init {
        qZoneBehavior = QZoneShareBehavior.MINI_PROGRAM
    }
}
