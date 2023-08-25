package com.pailan.ec4.data.retrofit

import com.pailan.ec4.data.response.ApiListResponse
import retrofit2.http.GET

interface ApiService {
    @GET("agents/")
    suspend fun getCupons(): ApiListResponse

    @GET("people/1/")
    suspend fun getFavorite(): ApiListResponse
}