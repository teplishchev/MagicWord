package com.example.magicwordsgameteplishchev.models.records

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "records")
data class Record (
    @PrimaryKey(autoGenerate = true) @NotNull val id: Int = 0,
    val difficulty: String = "",
    val attemptsNumber: Int = 0,
    val gameMode: String = "",
    val date: String = "",
    val time: String = "",
    val word: String =""
)