package com.example.magicwordsgameteplishchev.models.settings

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert
    fun insert(settings: Settings)

    @Update
    fun update(settings: Settings)

    @Delete
    fun delete(settings: Settings)

    @Query("SELECT * FROM settings")
    fun getSettings() : Flow<List<Settings>>

}