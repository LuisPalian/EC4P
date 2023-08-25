package com.pailan.ec4.data.repository

import com.pailan.ec4.data.db.ApiDao
import com.pailan.ec4.data.response.ApiListResponse
import com.pailan.ec4.data.response.ApiResponse
import com.pailan.ec4.data.retrofit.ServiceInstance
import com.pailan.ec4.model.Api

class ApisRepository (val apiDao: ApiDao? = null){
    suspend fun getCupons(): ApiResponse<ApiListResponse> {
        return try {
            val response = ServiceInstance.getMichiService().getCupons()
            ApiResponse.Success(response)
        }catch (e: Exception){
            ApiResponse.Error(e)
        }
    }
    suspend fun addFavorite(api: Api) {
        apiDao?.let {
            it.addFavorite(api)
        }
    }

    suspend fun removeFavorite(api: Api) {
        apiDao?.let {
            it.removeFavorite(api)
        }
    }
    fun getFavorites(): List<Api> {
        apiDao?.let {
            return it.getFavorites()
        } ?: kotlin.run {
            return listOf()
        }
    }
}