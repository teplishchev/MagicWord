package com.example.magicwordsgameteplishchev.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey val value: String = "",
    val difficulty: String = Difficulty.SIMPLE.name,
    val number_bulls: Int = 0,
    val number_cows: Int = 0,
    val lang: String = "",
    val size: Int = 0
    )