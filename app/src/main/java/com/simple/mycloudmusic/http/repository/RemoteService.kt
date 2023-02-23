package com.simple.mycloudmusic.http.repository

import com.simple.mycloudmusic.http.RetrofitService
import com.simple.mycloudmusic.http.service.LoginService


/**
 * @description
 * @author simple.wu
 * @date 2023/2/23
 */
object RemoteService {
    suspend fun loginByPhone(phone: String, password: String): Any? {
        val aaa = RetrofitService.getInstance().create(LoginService::class.java).loginByPhone(phone, password)
        return null
    }

    suspend fun checkMusic(phone: String): Any? {
        val aaa = RetrofitService.getInstance().create(LoginService::class.java).checkMusic(phone)
        return null
    }
}