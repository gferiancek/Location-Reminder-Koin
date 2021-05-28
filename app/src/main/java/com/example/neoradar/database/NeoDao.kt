package com.example.neoradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NeoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg neos: NeoEntity)

    @Query("SELECT * FROM neo_radar_table")
    fun getAllNeos(): LiveData<List<NeoEntity>>
}