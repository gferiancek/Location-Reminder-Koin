package com.example.neoradar.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.neoradar.database.asDomainModel
import com.example.neoradar.database.getDatabase
import com.example.neoradar.domain.Neo
import com.example.neoradar.repository.NeoRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

enum class NeoApiStatus { LOADING, DONE, ERROR }

class NeoListViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = NeoRepository(database)
    val imageOfTheDay = repository.imageOfTheDay
    val neoList = repository.neoList

    private val _eventNavigateToDetail = MutableLiveData<Neo?>()
    val eventNavigateToDetail: LiveData<Neo?>
        get() = _eventNavigateToDetail

    private val _status = MutableLiveData<NeoApiStatus>()
    val status: LiveData<NeoApiStatus>
        get() = _status

    /**
     * Function to set the UiState for the app.  If neoList.isNullOrEmpty, then we force a call
     * to the API so we have something to show the user. If the call fails for any reason, such as not
     * having and internet connection, we show and error screen. If neoList isn't NullOrEmpty, we just
     * show the cached data.
     */
    fun setUiState() {
        viewModelScope.launch {
            if (neoList.value.isNullOrEmpty()) {
                _status.value = NeoApiStatus.LOADING
                try {
                    repository.refreshNeoData()
                    _status.value = NeoApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = NeoApiStatus.ERROR
                }
            } else {
                _status.value = NeoApiStatus.DONE
            }
        }
    }

    fun navigateToDetail(currentNeo: Neo) {
        _eventNavigateToDetail.value = currentNeo
    }

    fun onNavigateToDetailFinished() {
        _eventNavigateToDetail.value = null
    }
}