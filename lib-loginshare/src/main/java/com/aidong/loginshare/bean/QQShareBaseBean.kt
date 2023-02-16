package com.aidong.loginshare.bean

import com.aidong.loginshare.share.QQShareBehavior
import com.aidong.loginshare.share.QZoneShareBehavior

/**
 * @description
 * @author simple.wu
 * @date 2023/2/8
 */
open class QQShareBaseBean {
    var behavior: QQShareBehavior? = null
    var qZoneBehavior: QZoneShareBehavior? = null
}