package com.example.magicwordsgameteplishchev.models.settings

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) @NotNull val id: Long,
    val lastVersionFB: Int = 0,
    val isMusicOn: Boolean = false,
    val musicLevel: Int = 50
)
