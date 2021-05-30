package com.example.neoradar.network

import com.example.neoradar.database.ImageOfTheDayEntity
import com.squareup.moshi.Json
import java.util.*

data class ImageOfTheDayDTO(
    @Json(name = "title")
    val title: String,
    @Json(name = "explanation")
    val description: String,
    @Json(name = "url")
    val url: String
)

/**
 * Date is used as the PrimaryKey and also as a way to check if we need to delete the image from the
 * database, so we use the NetworkUtils.calculateCurrentDate func to get the date at the time of
 * adding into database, which is when asDatabaseModel is called.
 */
fun ImageOfTheDayDTO.asDatabaseModel(): ImageOfTheDayEntity {
    return ImageOfTheDayEntity(
        date = calculateCurrentDate(Calendar.getInstance()),
        title = title,
        description = description,
        url = url
    )
}
