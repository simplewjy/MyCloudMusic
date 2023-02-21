package com.simple.mycloudmusic.http

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Admin
 * On 2021/5/7
 */
class RetrofitService {

    private lateinit var mRetrofit: Retrofit


    constructor() {
        build()
    }

    companion object {


        @JvmField
        var BaseUrl: String = "https://neteasecloud.moonsimple.space/"

        //默认连接超时时间
        const val DEFAULT_CONNECT_TIME_OUT = 15L

        //默认读数据超时时间
        const val DEFAULT_READ_TIME_OUT = 15L

        //单例
        private var instance: RetrofitService? = null

        /**
         * 获取单例
         */
        fun getInstance(url: String?): RetrofitService {
            synchronized(RetrofitService::class.java) {
                if (instance == null || url != BaseUrl) {
                    instance = RetrofitService()
                }
                return instance!!
            }
        }

    }

    private fun build() {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * create
     */
    private fun <T> create(service: Class<T>): T {
        return mRetrofit.create(service)
    }


    /**
     * 获取okHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient()
            .newBuilder()
            .hostnameVerifier { hostname, session ->
                true
            }
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.i("http", message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        okHttpClientBuilder.connectTimeout(DEFAULT_CONNECT_TIME_OUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
        return okHttpClientBuilder.build()
    }


}