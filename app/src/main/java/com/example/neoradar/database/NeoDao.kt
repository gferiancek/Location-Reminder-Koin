package com.example.neoradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NeoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNeos(vararg neos: NeoEntity)

    @Query("SELECT * FROM neo_radar_table ORDER BY close_approach_date ASC")
    fun getAllNeos(): LiveData<List<NeoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageOfTheDay(imageOfTheDayEntity: ImageOfTheDayEntity)

    @Query("SELECT * FROM image_of_the_day LIMIT 1")
    fun getImageOfTheDay(): LiveData<ImageOfTheDayEntity?>
}