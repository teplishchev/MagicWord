package com.example.magicwordsgameteplishchev.models

import android.content.Context
import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magicwordsgameteplishchev.models.settings.Settings
import com.example.magicwordsgameteplishchev.models.settings.SettingsDao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WordViewModel() : ViewModel() {

    private lateinit var context: Context
    private lateinit var dbInstance: WordsDatabase
    lateinit var currentWord: LiveData<Word>
    private val database = Firebase.database

    @RequiresApi(Build.VERSION_CODES.N)
    fun setContextLate(context: Context) {
        this.context = context
        dbInstance = WordsDatabase.getInstance(context)

        //val s = Settings(1, 1, true, 50)
        //DeleteSettingAsyncTask(dbInstance.settingsDao()).execute(s)
        viewModelScope.launch(Dispatchers.IO) {
            dbInstance.settingsDao().getSettings().collect { settings ->
                val setting = if (settings.size > 0) settings[0] else null
                checkFBDBChanges(setting) { pairs ->
                    if (setting != null) {
                        if (pairs.size > 0) {
                            //Update values all from our last to Fb last version
                            updateWords(pairs)
                            val maxId = findLastUpdate(pairs)
                            val newSetting =
                                Settings(setting.id, maxId, setting.isMusicOn, setting.musicLevel)
                            UpdateSettingAsyncTask(dbInstance.settingsDao()).execute(newSetting)
                        }
                    } else {
                        //Download all data from FB
                        updateWords(pairs)
                        val maxId = findLastUpdate(pairs)
                        val newSetting = Settings(0, maxId, true, 50)
                        InsertSettingAsyncTask(dbInstance.settingsDao()).execute(newSetting)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun findLastUpdate(pairs: List<Pair<Int, String>>): Int {
        var max = 0
        if (pairs.size > 0) {
            return pairs.stream().sorted(object : Comparator<Pair<Int, String>> {
                override fun compare(
                    p0: Pair<Int, String>?,
                    p1: Pair<Int, String>?
                ): Int {
                    if (p0?.first!! > p1?.first!!) {
                        return -1
                    } else if (p0?.first!! < p1?.first!!) {
                        return 1
                    } else {
                        return 0
                    }

                }
            }).findFirst().get().first
        }
        return max
    }

    fun updateWords(pairs: List<Pair<Int, String>>) {
        for (pair in pairs) {
            val ref =
                database.reference.child("Words").child(pair.second).child(pair.first.toString())
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val wordValue = data.child("value").value
                        val wordDifficulty = data.child("difficulty").value
                        val lang = data.child("lang").value
                        val size = data.child("size").value.toString()
                        addWord(
                            Word(
                                wordValue as String, wordDifficulty as String,
                                0, 0, lang as String, size.toInt()
                            )
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun checkFBDBChanges(setting: Settings?, callback: (List<Pair<Int, String>>) -> Unit) {
        val reference = database.reference.child("Settings")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listNewSett = mutableListOf<Pair<Int, String>>()
                for (data in snapshot.children) {
                    val numberSett = data.key.toString()
                    if (setting != null) {
                        if (numberSett > setting?.lastVersionFB.toString()) {
                            val path = data.value.toString()
                            listNewSett.add(Pair(numberSett.toInt(), path))
                        }
                    } else {
                        val path = data.value.toString()
                        listNewSett.add(Pair(numberSett.toInt(), path))
                    }
                }
                callback(listNewSett)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getRandomWord(complexity: String) {
        currentWord = dbInstance.wordDao().getRandomWord(complexity)
    }

    fun checkWordExisting(value: String): LiveData<Boolean> {
        return dbInstance.wordDao().checkWordInDB(value)
    }

    fun addWord(word: Word) {
        InsertWordAsyncTask(dbInstance.wordDao()).execute(word)
    }

    fun deleteAll() {
        DeleteAllWordsAsyncTask(dbInstance.wordDao()).execute()
    }

    private class InsertWordAsyncTask internal constructor(wordDao: WordDao) :
        AsyncTask<Word, Void?, Void>() {
        private val wordDao: WordDao
        override fun doInBackground(vararg word: Word): Void? {
            wordDao.insert(word[0])
            return null;
        }

        init {
            this.wordDao = wordDao
        }
    }

    private class InsertSettingAsyncTask internal constructor(settDao: SettingsDao) :
        AsyncTask<Settings, Void?, Void>() {
        private val settDao: SettingsDao
        override fun doInBackground(vararg sett: Settings): Void? {
            settDao.insert(sett[0])
            return null;
        }

        init {
            this.settDao = settDao
        }
    }

    private class UpdateSettingAsyncTask internal constructor(settDao: SettingsDao) :
        AsyncTask<Settings, Void?, Void>() {
        private val settDao: SettingsDao
        override fun doInBackground(vararg sett: Settings): Void? {
            settDao.update(sett[0])
            return null;
        }

        init {
            this.settDao = settDao
        }
    }

    private class DeleteSettingAsyncTask internal constructor(settDao: SettingsDao) :
        AsyncTask<Settings, Void?, Void>() {
        private val settDao: SettingsDao
        override fun doInBackground(vararg sett: Settings): Void? {
            settDao.delete(sett[0])
            return null;
        }

        init {
            this.settDao = settDao
        }
    }

    private class DeleteAllWordsAsyncTask internal constructor(wordDao: WordDao) :
        AsyncTask<Void?, Void?, Void>() {
        private val wordDao: WordDao
        override fun doInBackground(vararg w: Void?): Void? {
            wordDao.deleteAll()
            return null;
        }

        init {
            this.wordDao = wordDao
        }
    }

}