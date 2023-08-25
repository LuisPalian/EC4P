package com.pailan.ec4.view.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pailan.ec4.data.repository.ApisRepository
import com.pailan.ec4.data.response.ApiResponse
import com.pailan.ec4.model.Api
import com.pailan.ec4.model.getData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiListViewModel : ViewModel() {
    val repository = ApisRepository()
    val cuponList: MutableLiveData<List<Api>> = MutableLiveData<List<Api>>()

    fun getCuponList(){
        val data = getData()
        cuponList.value = data
    }


    fun getCuponsFromService(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCupons()
            when(response){
                is ApiResponse.Error ->{
                }
                is ApiResponse.Success ->{
                    cuponList.postValue(response.data.data)
                }
            }
        }
    }

}