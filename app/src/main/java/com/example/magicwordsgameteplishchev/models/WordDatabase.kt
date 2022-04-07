package com.example.magicwordsgameteplishchev.models

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.magicwordsgameteplishchev.models.records.Record
import com.example.magicwordsgameteplishchev.models.records.RecordDao
import com.example.magicwordsgameteplishchev.models.settings.Settings
import com.example.magicwordsgameteplishchev.models.settings.SettingsDao

@Database(entities = [Word::class, Record::class, Settings::class],
    version = 4)
abstract class WordsDatabase : RoomDatabase() {
    abstract fun wordDao() : WordDao
    abstract fun recordDao() : RecordDao
    abstract fun settingsDao() : SettingsDao

    companion object {
        private const val DB_NAME = "MagicWords.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `records` (`id` INTEGER NOT NULL, `difficulty` TEXT NOT NULL, " +
                        "`attemptsNumber` INTEGER NOT NULL, `gameMode` TEXT NOT NULL, `date` TEXT NOT NULL," +
                        "`time` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE records ADD COLUMN 'word' TEXT NOt Null Default('word')")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `settings` (`id` INTEGER NOT NULL, `lastVersionFB` INTEGER NOT NULL, " +
                        "`isMusicOn` INTEGER NOT NULL, `musicLevel` INTEGER NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("ALTER TABLE words ADD COLUMN 'lang' TEXT NOt Null Default('ru')")
                database.execSQL("ALTER TABLE words ADD COLUMN 'size' INTEGER NOt Null Default(1)")
            }
        }

        fun getInstance(context: Context) : WordsDatabase {
            return Room.databaseBuilder(context, WordsDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build()
        }
    }
}