package com.aidong.loginshare.login

/**
 * @description qq登录的几种方式
 * @author simple.wu
 * @date 2023/2/7
 */
enum class QQLoginMode {
    //CLIENT_QRCODE,表示有客户端时跳转到客户端登录页面登录，否则跳转到扫码页面
    CLIENT_QRCODE,
    //CLIENT_WEB,表示有客户端时跳转到客户端登录页面登录，否则跳转到web登录页面登录
    CLIENT_WEB,
    //QRCODE,表示仅支持扫码登录
    QRCODE
}