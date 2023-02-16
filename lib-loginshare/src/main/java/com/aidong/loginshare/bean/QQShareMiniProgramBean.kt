package com.aidong.loginshare.bean

import com.aidong.loginshare.share.QQShareBehavior
import com.tencent.connect.share.QQShare

/**
 * @description
 * @author simple.wu
 * @date 2023/2/9
 */
data class QQShareMiniProgramBean(
    val shareToQQTitle: String = "",
    val shareToQQSummary: String = "",
    val shareToQQTargetUrl: String = "",
    val shareToQQImgUrl: String = "",
    val shareToQQMiniProgramAppId: String = "",
    val shareToQQMiniProgramPath: String = "",
    val shareToQQMiniProgramType: String = ""
): QQShareBaseBean() {
    init {
        behavior = QQShareBehavior.MINI_PROGRAM
    }
}
