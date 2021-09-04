package com.udacity.project4.domain.model

sealed class DataState<T> {

    data class Data<T>(
        val data: T? = null
    ) : DataState<T>()

    data class Error<T>(
        val message: String
    ) : DataState<T>()

    data class Loading<T>(
        val loading: Boolean = true
    ) : DataState<T>()
}