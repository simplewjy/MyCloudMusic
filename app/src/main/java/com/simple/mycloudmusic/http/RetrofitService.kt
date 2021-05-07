package com.simple.mycloudmusic.http

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Admin
 * On 2021/5/7
 */
class RetrofitService {

    private lateinit var mRetrofit: Retrofit

    private var instance: RetrofitService? = null

    constructor() {
        build()
    }

    companion object {

        @JvmField
        var BaseUrl: String = ""


    }

    private fun build() {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun setBaseUrl(baseUrl: String) {

    }

    /**
     * 获取单例
     */
    private fun getInstance(): RetrofitService {
        synchronized(RetrofitService::class.java) {
            if (instance == null) {
                instance = RetrofitService()
            }
            return instance!!
        }
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
            Log.i("cloudMusic==========>", message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        return okHttpClientBuilder.build()
    }


}