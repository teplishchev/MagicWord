package com.example.magicwordsgameteplishchev

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.magicwordsgameteplishchev.models.Word

class WordsAdapter(val context: Context, val words: List<Word>) : RecyclerView.Adapter<WordsAdapter.WordViewHolder>() {

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text_number_cow = itemView.findViewById<TextView>(R.id.word_with_answer_item_number_cow)
        val text_number_bull = itemView.findViewById<TextView>(R.id.word_with_answer_item_number_bull)
        val text_word = itemView.findViewById<TextView>(R.id.word_with_answer_item_word)

        fun bindItem(word: Word) {
            text_word.text = word.value
            text_number_bull.text = word.number_bulls.toString()
            text_number_cow.text = word.number_cows.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.word_with_answer_item, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.bindItem(word)
    }

    override fun getItemCount(): Int {
        return words.size
    }

}