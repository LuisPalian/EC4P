package com.pailan.ec4.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://valorant-api.com/v1/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getMichiService(): ApiService = retrofit.create(ApiService::class.java)
}