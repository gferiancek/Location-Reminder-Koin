package com.udacity.project4.di

import com.udacity.project4.domain.worker.DeleteWorker
import com.udacity.project4.domain.worker.GeofenceWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker { GeofenceWorker(get(), get(), get()) }
    worker { DeleteWorker(get(), get(), get()) }
}