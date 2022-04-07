package com.example.magicwordsgameteplishchev.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    @Update
    fun update(word: Word)

    @Delete
    fun delete(word: Word)

    @Query("DELETE FROM words")
    fun deleteAll()

    @Query("SELECT * FROM words WHERE difficulty = :complexity ORDER BY RANDOM() LIMIT 1")
    fun getRandomWord(complexity: String) : LiveData<Word>

    @Query("SELECT EXISTS(SELECT * FROM words WHERE value = :word)")
    fun checkWordInDB(word: String) : LiveData<Boolean>
}