package com.pailan.ec4.view.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pailan.ec4.data.db.ApiDataBase
import com.pailan.ec4.data.repository.ApisRepository
import com.pailan.ec4.model.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiFavoriteViewModel (application: Application) : AndroidViewModel(application){
    private val repository: ApisRepository
    private val _favorites: MutableLiveData<List<Api>> = MutableLiveData()
    val favorites: LiveData<List<Api>> = _favorites

    init {
        val db = ApiDataBase.getDatabase(application)
        repository = ApisRepository(db.apiDao())
        refreshFavorites()
    }

    fun refreshFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getFavorites()
            withContext(Dispatchers.Main) {
                _favorites.value = data
            }
        }
    }
}