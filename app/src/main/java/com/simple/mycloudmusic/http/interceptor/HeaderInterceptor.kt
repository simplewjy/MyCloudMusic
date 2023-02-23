package com.simple.mycloudmusic.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @description
 * @author simple.wu
 * @date 2023/2/23
 */
class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.newBuilder().addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
        return chain.proceed(request)
    }
}