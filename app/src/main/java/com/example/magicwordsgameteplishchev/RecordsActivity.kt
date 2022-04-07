package com.example.magicwordsgameteplishchev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magicwordsgameteplishchev.models.GameMode
import com.example.magicwordsgameteplishchev.models.WordViewModel
import com.example.magicwordsgameteplishchev.models.records.RecordViewModel

class RecordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        val recordsViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        recordsViewModel?.setContextLate(this)

        val recordsRecycler = findViewById<RecyclerView>(R.id.records_recycler)
        var adapter: RecordsAdapter
        recordsRecycler.layoutManager = LinearLayoutManager(this)

        recordsViewModel.getListRecords(GameMode.FOUR_LETTERS_SIMPLE.name).observe(this, Observer {
            val sortList = it.sortedBy { it.attemptsNumber }
            adapter = RecordsAdapter(this, sortList)
            recordsRecycler.adapter = adapter
        })
    }
}