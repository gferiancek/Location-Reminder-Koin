package com.example.neoradar.repository

import androidx.lifecycle.Transformations
import com.example.neoradar.BuildConfig
import com.example.neoradar.database.NeoDatabase
import com.example.neoradar.database.asDomainModel
import com.example.neoradar.network.NasaApi
import com.example.neoradar.network.asDatabaseModel
import com.example.neoradar.network.calculateCurrentDate
import com.example.neoradar.network.parseNeoJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class NeoRepository(val database: NeoDatabase) {

    val neoList = Transformations.map(database.neoDao.getAllNeos()) {
        it.asDomainModel()
    }
    val imageOfTheDay = Transformations.map(database.neoDao.getImageOfTheDay()) {
        it?.asDomainModel()
    }

    suspend fun refreshNeoData() {
        withContext(Dispatchers.IO) {
            fetchImageOfTheDay()
            fetchNeoList()
        }
    }

    /**
     * Lessons used a Deferred<Type> as the return type in our API Service, and would call .await() at the end
     * of the Retrofit call. (In this case, this is in the val neoJsonObject declaration).  However, since 2.6.0
     * Retrofit has support for suspend functions so that is no longer needed.  NasaApi.nasaRetrofit.getNeoJsonResult()
     * is a suspend fun, so calling .await() is not necessary!
     */
    suspend fun fetchNeoList() {
        val neoJsonObject = JSONObject(
            NasaApi.nasaRetrofit.getNeoJsonResult(
                BuildConfig.NEOWS_API_KEY, calculateCurrentDate(Calendar.getInstance())
            )
        )
        val neoList = parseNeoJsonResult(neoJsonObject).asDatabaseModel()
        database.neoDao.insertAllNeos(*neoList)
    }

    /**
     * Calls and stores ImageOfTheDay object in database. Uses suspend instead of Deferred/Await as noted above
     */
    suspend fun fetchImageOfTheDay() {
        val imageOfTheDayEntity =
            NasaApi.nasaRetrofit.getPictureOfTheDay(BuildConfig.NEOWS_API_KEY).asDatabaseModel()
        database.neoDao.insertImageOfTheDay(imageOfTheDayEntity)
    }

    suspend fun deleteImageOfTheDay(date: String) {
        database.neoDao.deleteImageOfTheDay(date)
    }

    suspend fun deleteNeoDatabase(date: String) {
        database.neoDao.deleteNeos(date)
    }

}