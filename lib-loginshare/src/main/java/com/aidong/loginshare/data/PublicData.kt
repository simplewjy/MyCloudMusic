package com.aidong.loginshare.data

/**
 * @description
 * @author simple.wu
 * @date 2023/1/6
 */
object PublicData {
    var WX_APP_ID = "wx365ab323b9269d30"
    var WX_APP_SECRET = ""
    var ALI_ONE_KEY_LOGIN_SECRET =
        "cYP6363cbZFFs2RWXlH06YHpp1rCidKoQkVnbnrF0LLidKGD5A+S5HFkgS2BQ51MHELWS+31xvSZGFMQUcDfcpTC1H8hoUjZ9VBvpimdAayWppFGAGM+vNe6As+4LiVEWRCx+70brElSHCiXo6pADDyRPnz/iRVyip2+I+6jKdlAkQxSBv781k3CvOyCj9R3sr+giSn4ngcUIzbDpWlXrqv4OVYda9c0Z4PBDxSXdGVV6g2BLIfIOXdVcVBYyrUTz0jtfnbDWzMEbeYibnLwnvpPj9bCKl4prWzsRDiXUjN57R89ABqdXw=="

    //在微博开发平台为应用申请的App Key
    var WEIBO_APP_KEY = "3088278870"

    //在微博开放平台设置的授权回调页
    const val REDIRECT_URL = "http://www.sina.com"

    //在微博开放平台为应用申请的高级权限
    const val SCOPE = ("email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write")
    var ALI_PAY_AUTH_INFO =
        "service=mobile.securitypay.pay&partner=2088021345411340&_input_charset=utf-8&notify_url=http%3A%2F%2Ffitness.aidong.me%2Fcallback%2Falipay&out_trade_no=20230116020339&subject=%E7%88%B1%E5%8A%A8-%E8%AF%BE%E7%A8%8B%E9%A2%84%E7%BA%A6-20230116020339&body=%E7%88%B1%E5%8A%A8-%E8%AF%BE%E7%A8%8B%E9%A2%84%E7%BA%A6-20230116020339&payment_type=1&total_fee=200.0&seller_id=2088021345411340&it_b_pay=30m&sign_type=RSA&sign=VbXUjRWV7cT5SaekXuA7cXssWSdy8x2rnL36aXO9uAJyOFHvsKuiFBpBS43KKFGhavHjvR2pmAw%2BX52jPJG7zrubxlX6ljSPisEguuWM%2FFPofnjPkIDgcR2VOoyeOYl5TctYssrZSSxcrHz8IE3aj4P3LR%2FH%2BW9PV4q4t07MKoA%3D"

    var ALI_PAY_ORDER_INFO =
        "service=mobile.securitypay.pay&partner=2088021345411340&_input_charset=utf-8&notify_url=http%3A%2F%2Ffitness.aidong.me%2Fcallback%2Falipay&out_trade_no=20230116020339&subject=%E7%88%B1%E5%8A%A8-%E8%AF%BE%E7%A8%8B%E9%A2%84%E7%BA%A6-20230116020339&body=%E7%88%B1%E5%8A%A8-%E8%AF%BE%E7%A8%8B%E9%A2%84%E7%BA%A6-20230116020339&payment_type=1&total_fee=200.0&seller_id=2088021345411340&it_b_pay=30m&sign_type=RSA&sign=VbXUjRWV7cT5SaekXuA7cXssWSdy8x2rnL36aXO9uAJyOFHvsKuiFBpBS43KKFGhavHjvR2pmAw%2BX52jPJG7zrubxlX6ljSPisEguuWM%2FFPofnjPkIDgcR2VOoyeOYl5TctYssrZSSxcrHz8IE3aj4P3LR%2FH%2BW9PV4q4t07MKoA%3D"

    //接入qq appId
    var QQ_APP_ID = "222222"
}