package com.example.neoradar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoradar.BuildConfig
import com.example.neoradar.network.NasaApi
import com.example.neoradar.network.NeoDTO
import com.example.neoradar.network.parseNeoJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class NeoListViewModel: ViewModel() {

    private val _neoList = MutableLiveData<List<NeoDTO>>()
    val neoList: LiveData<List<NeoDTO>>
        get() = _neoList

    init {
        viewModelScope.launch {
            val neoJSON = NasaApi.nasaRetrofit.getNeoJsonResult(BuildConfig.NEOWS_API_KEY)
            _neoList.value = parseNeoJsonResult(JSONObject(neoJSON))
        }
    }
}