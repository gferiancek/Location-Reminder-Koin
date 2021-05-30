package com.example.neoradar.network

import com.example.neoradar.database.ImageOfTheDayEntity
import com.squareup.moshi.Json

data class ImageOfTheDayDTO(
    @Json(name = "title")
    val title: String,
    @Json(name = "explanation")
    val description: String,
    @Json(name = "url")
    val url: String)

fun ImageOfTheDayDTO.asDatabaseModel(): ImageOfTheDayEntity {
    return ImageOfTheDayEntity(
        title = title,
        description = description,
        url = url)
}
