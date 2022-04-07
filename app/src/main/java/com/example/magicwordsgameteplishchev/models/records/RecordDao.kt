package com.example.magicwordsgameteplishchev.models.records

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecordDao {

    @Insert
    fun insert(record: Record)

    @Update
    fun update(record: Record)

    @Delete
    fun delete(record: Record)

    @Query("DELETE FROM records")
    fun deleteAll()

    @Query("SELECT * FROM records WHERE gameMode = :gameMode")
    fun findRecordsByCategory(gameMode: String) : LiveData<List<Record>>
}