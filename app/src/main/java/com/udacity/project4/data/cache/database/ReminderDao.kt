package com.udacity.project4.data.cache.database

import androidx.room.*
import com.udacity.project4.data.cache.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminder(reminder: ReminderEntity)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity): Int

    @Query("SELECT * FROM reminders ORDER BY id ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id LIKE :id")
    suspend fun getReminder(id: String): ReminderEntity?

    @Query("DELETE FROM reminders WHERE id LIKE :id")
    suspend fun deleteReminder(id: String)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()

}