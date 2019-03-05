package com.yuliyang.well_design

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    private val retrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {

        val okhttpClient = OkHttpClient.Builder()//
            .connectTimeout(5, TimeUnit.SECONDS)//
            .readTimeout(30, TimeUnit.SECONDS)//
            .writeTimeout(30, TimeUnit.SECONDS)

        Retrofit.Builder()//
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://www.baidu.com/")//
            .client(okhttpClient.build())//
            .build()
    }

    /**
     * 生成对应的Service实例
     */
    fun <T> createService(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

}