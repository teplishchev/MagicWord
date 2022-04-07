package com.example.magicwordsgameteplishchev.models.records

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.magicwordsgameteplishchev.models.WordsDatabase

class RecordViewModel() : ViewModel() {

    private lateinit var context: Context
    private lateinit var dbInstance: WordsDatabase
    var isRecordDone : Boolean = false

    fun setContextLate(context: Context) {
        this.context = context
        dbInstance = WordsDatabase.getInstance(context)
    }

    fun getListRecords(gameMode: String): LiveData<List<Record>> {
        return dbInstance.recordDao().findRecordsByCategory(gameMode)
    }

    fun addRecord(record: Record) {
        InsertRecordAsyncTask(dbInstance.recordDao()).execute(record)
    }

    fun deleteRecord(record: Record) {
        DeleteRecordAsyncTask(dbInstance.recordDao()).execute(record)
    }

    fun deleteAll() {
        DeleteAllRecordsAsyncTask(dbInstance.recordDao()).execute()
    }

    fun isCheckRecords() {
        this.isRecordDone = true
    }

    private class InsertRecordAsyncTask internal constructor(recordDao: RecordDao) :
        AsyncTask<Record, Void?, Void>() {
        private val recordDao: RecordDao
        override fun doInBackground(vararg record: Record): Void? {
            recordDao.insert(record[0])
            return null;
        }

        init {
            this.recordDao = recordDao
        }
    }

    private class DeleteRecordAsyncTask internal constructor(recordDao: RecordDao) :
        AsyncTask<Record, Void?, Void>() {
        private val recordDao: RecordDao
        override fun doInBackground(vararg record: Record): Void? {
            recordDao.delete(record[0])
            return null;
        }

        init {
            this.recordDao = recordDao
        }
    }

    private class DeleteAllRecordsAsyncTask internal constructor(recordDao: RecordDao) :
        AsyncTask<Void?, Void?, Void>() {
        private val recordDao: RecordDao
        override fun doInBackground(vararg r: Void?): Void? {
            recordDao.deleteAll()
            return null;
        }

        init {
            this.recordDao = recordDao
        }
    }

}