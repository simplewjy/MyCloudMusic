package com.simple.mycloudmusic.http.service

import com.simple.mycloudmusic.bean.BaseBean
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @description
 * @author simple.wu
 * @date 2023/2/23
 */
interface LoginService {
    /**
     * 手机登录(现在要求验证,暂时绕不过,请使用二维码登录)
     */
    @POST("login/cellphone")
    suspend fun loginByPhone(@Query("phone") phone: String, @Query("password") password: String): BaseBean<Any>


    @POST("check/music")
    suspend fun checkMusic(@Query("id") id: String): BaseBean<Any?>
}