package com.example.locationreminder.cache.database

import androidx.room.*
import com.example.locationreminder.cache.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders ORDER BY id ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id LIKE :id")
    suspend fun getReminder(id: Long): ReminderEntity

    @Query("DELETE FROM reminders WHERE id LIKE :id")
    suspend fun deleteReminder(id: Long)
}