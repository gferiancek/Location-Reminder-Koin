package com.example.neoradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.neoradar.domain.ImageOfTheDay

@Entity(tableName = "image_of_the_day")
data class ImageOfTheDayEntity(
    @PrimaryKey
    val date: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "url")
    val url: String
)

fun ImageOfTheDayEntity.asDomainModel(): ImageOfTheDay {
    return ImageOfTheDay(
        url = url,
        contentDescription = "$title. $description"
    )
}
