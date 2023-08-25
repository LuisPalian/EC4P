package com.pailan.ec4.view.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pailan.ec4.data.db.ApiDataBase
import com.pailan.ec4.data.repository.ApisRepository
import com.pailan.ec4.model.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiDetailViewModel (application: Application) : AndroidViewModel(application){
    private val repository: ApisRepository
    private val favoriteApi: MutableSet<String> = mutableSetOf()

    init {
        val db = ApiDataBase.getDatabase(application)
        repository = ApisRepository(db.apiDao())
        // Llena el conjunto de favoritos al inicio
        viewModelScope.launch(Dispatchers.IO) {
            val favorites = repository.getFavorites()
            favoriteApi.addAll(favorites.map { it.displayName })
        }
    }

    fun addFavorite(api: Api) {
        if (!favoriteApi.contains(api.displayName)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addFavorite(api)
                favoriteApi.add(api.displayName)
            }
        }
    }

    fun removeFavorite(api: Api) {
        if (favoriteApi.contains(api.displayName)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.removeFavorite(api)
                favoriteApi.remove(api.displayName)
            }
        }
    }

    fun isFavorite(api: Api): Boolean {
        return api.displayName in favoriteApi
    }

}