package com.example.magicwordsgameteplishchev

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.magicwordsgameteplishchev.models.Word
import com.example.magicwordsgameteplishchev.models.records.Record

class RecordsAdapter(val context: Context, val records: List<Record>) : RecyclerView.Adapter<RecordsAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val text_word = view.findViewById<TextView>(R.id.record_item_text_word)
        val text_count = view.findViewById<TextView>(R.id.record_item_text_count)
        val text_place = view.findViewById<TextView>(R.id.record_item_text_place)

        fun bindViewHolder(record: Record, place: String) {
            text_count.setText(record.attemptsNumber.toString())
            text_word.setText(record.word)
            text_place.setText(place)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.record_item, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]
        holder.bindViewHolder(record, (position+1).toString())
    }

    override fun getItemCount(): Int {
        return records.size
    }

}