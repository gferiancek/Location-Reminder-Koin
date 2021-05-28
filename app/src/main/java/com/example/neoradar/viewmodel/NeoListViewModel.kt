package com.example.neoradar.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.neoradar.database.getDatabase
import com.example.neoradar.repository.NeoRepository
import kotlinx.coroutines.launch

class NeoListViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = NeoRepository(database)

    val neoList = repository.neoList

    init {
        viewModelScope.launch {
            repository.refreshNeoList()
        }
    }
}