package com.example.neoradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getNeoJsonResult(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String): ImageOfTheDayDTO
}

/**
 * Since version 2.6.0 Retrofit has built in support for suspend functions, so we no longer need to add
 * the CoroutineCallAdapterFactory in our RetrofitBuilder.  It also allows us to use a suspend fun instead
 * of a Deferred<Type> in the above NasaApiService @GET functions.
 */
object NasaApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val nasaRetrofit: NasaApiService = retrofit.create(NasaApiService::class.java)
}