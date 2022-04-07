package com.example.magicwordsgameteplishchev

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.set
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magicwordsgameteplishchev.models.*
import com.example.magicwordsgameteplishchev.models.records.Record
import com.example.magicwordsgameteplishchev.models.records.RecordViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "mode"
private const val ARG_PARAM2 = "numberAttempts"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {

    private var mode: String? = null
    private var numberAttempts: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(ARG_PARAM1)
            numberAttempts = it.getInt(ARG_PARAM2)
        }
    }

    fun fillTextAttempts(textView: TextView) {
        if (mode == "classic") {
            textView.setText(R.string.fragment_game_attempts_text)
            val text = textView.text.toString()
            textView.text = "$text 0"
        } else {
            textView.setText(R.string.fragment_game_attempts_to_end_text)
            val text = textView.text.toString()
            textView.text = "$text $numberAttempts"
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        val wordsViewModel = activity?.let { ViewModelProvider(it).get(WordViewModel::class.java) }
        val recordsViewModel = activity?.let { ViewModelProvider(it).get(RecordViewModel::class.java) }
        activity?.let { wordsViewModel?.setContextLate(it.application) }
        activity?.let { recordsViewModel?.setContextLate(it.application) }

        if (savedInstanceState == null) {
            wordsViewModel?.getRandomWord("S")
        }

        val edit_full = view.findViewById<EditText>(R.id.game_edit_full)
        val recyclerView = view.findViewById<RecyclerView>(R.id.game_words_recycler)
        val btn_send_answer = view.findViewById<Button>(R.id.game_btn_send)
        val text_num_attempts = view.findViewById<TextView>(R.id.game_text_attempts)
        fillTextAttempts(text_num_attempts)

        var mutableWordsList = mutableListOf<Word>()
        recyclerView.layoutManager = LinearLayoutManager(activity).apply {
            setReverseLayout(true)
            setStackFromEnd(true)
        }
        val wordsAdapter = activity?.let {
            WordsAdapter(it.applicationContext, mutableWordsList)
        }
        recyclerView.adapter = wordsAdapter

        btn_send_answer.setOnClickListener(View.OnClickListener {

            if (edit_full.text.isBlank()) {
                Toast.makeText(activity, "Заполните все буквы слова...", Toast.LENGTH_SHORT).show()
            } else {
                val wordValue = edit_full.text.toString()
                edit_full.text.clear()
                val call = {w: Word ->
                    mutableWordsList.add(w)
                    if (mode == "classic") {
                        val text = text_num_attempts.text.toString().split(":")[0]
                        val newText = text + ": ${mutableWordsList.size}"
                        text_num_attempts.setText(newText)
                    } else {
                        val attemptsLeft = numberAttempts?.minus(mutableWordsList.size)
                        val text = text_num_attempts.text.toString().split(":")[0]
                        val newText = text + ": ${attemptsLeft}"
                        text_num_attempts.setText(newText)
                        if (attemptsLeft == 0) {
                            var dialog: AlertDialog? = null
                            val builder = AlertDialog.Builder(activity)
                            builder.setCancelable(true)
                            var view: View = LayoutInflater.from(activity)
                                .inflate(R.layout.dialog_loss_game, null)
                            val btn_ok = view.findViewById<Button>(R.id.dialog_win_btn_ok)
                            val btn_main = view.findViewById<Button>(R.id.dialog_win_btn_main)

                            btn_ok.setOnClickListener(View.OnClickListener {
                                mutableWordsList.clear()
                                wordsAdapter?.notifyDataSetChanged()
                                wordsViewModel?.getRandomWord("S")
                                fillTextAttempts(text_num_attempts)
                                dialog?.dismiss()
                            })

                            btn_main.setOnClickListener(View.OnClickListener {
                                startActivity(Intent(activity, MainActivity::class.java))
                                dialog?.dismiss()
                            })
                            //endregion
                            builder.setView(view)
                            dialog = builder.create()
                            dialog.show()
                        }
                    }
                    wordsAdapter?.notifyDataSetChanged()
                    if (w.number_bulls == 4 && w.number_cows == 4) {
                        activity?.let { activityF ->
                            recordsViewModel?.getListRecords(GameMode.FOUR_LETTERS_SIMPLE.name)?.observe(
                                activityF, Observer {
                                    listRecords ->
                                    val dateUtil = DateUtil()
                                    val date = dateUtil.getCurrentDate() ?: ""
                                    val record = Record(0,"S", mutableWordsList.size,
                                        GameMode.FOUR_LETTERS_SIMPLE.name, date, "0", w.value)
                                    if (!recordsViewModel.isRecordDone) {
                                        if (listRecords.size < 10) {
                                            recordsViewModel.addRecord(record)
                                        } else {
                                            val minRecord =
                                                listRecords.sortedWith(object : Comparator<Record> {
                                                    override fun compare(
                                                        p0: Record?,
                                                        p1: Record?
                                                    ): Int {
                                                        if (p1 != null && p0 != null) {
                                                            if (p0.attemptsNumber > p1.attemptsNumber) {
                                                                return 1
                                                            } else if (p0.attemptsNumber < p1.attemptsNumber) {
                                                                return -1
                                                            } else {
                                                                return 0
                                                            }
                                                        } else {
                                                            return 0
                                                        }
                                                    }
                                                }).get(listRecords.lastIndex)
                                            if (minRecord.attemptsNumber > record.attemptsNumber) {
                                                recordsViewModel.deleteRecord(minRecord)
                                                recordsViewModel.addRecord(record)
                                            }
                                        }
                                        recordsViewModel.isCheckRecords()
                                    }
                                })
                        }
                        var dialog: AlertDialog? = null
                        val builder = AlertDialog.Builder(activity)
                        builder.setCancelable(true)
                        var view: View = LayoutInflater.from(activity)
                            .inflate(R.layout.dialog_win, null)
                        val btn_ok = view.findViewById<Button>(R.id.dialog_win_btn_ok)
                        val btn_main = view.findViewById<Button>(R.id.dialog_win_btn_main)

                        btn_ok.setOnClickListener(View.OnClickListener {
                            mutableWordsList.clear()
                            wordsAdapter?.notifyDataSetChanged()
                            wordsViewModel?.getRandomWord("S")
                            fillTextAttempts(text_num_attempts)
                            dialog?.dismiss()
                        })

                        btn_main.setOnClickListener(View.OnClickListener {
                            startActivity(Intent(activity, MainActivity::class.java))
                            dialog?.dismiss()
                        })
                        //endregion
                        builder.setView(view)
                        dialog = builder.create()
                        dialog.show()
                        //Toast.makeText(activity, "Вы угадали слово!!!", Toast.LENGTH_SHORT).show()
                    }
                }

                wordsViewModel?.let { viewModel -> checkWord(wordValue, viewModel, call) }

            }
        })

//        edit_first.addTextChangedListener(object : TextWatcher{
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (edit_first.text.length == 1) {
//                    if (edit_second.text.length == 1) {
//                        if (edit_third.text.length == 1) {
//                            if (edit_fofth.text.length != 1) {
//                                edit_fofth.requestFocus()
//                            }
//                        } else {
//                            edit_third.requestFocus()
//                        }
//                    } else {
//                        edit_second.requestFocus()
//                    }
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//
//            }
//        })

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkWord(value : String, viewModel: WordViewModel, call: (Word) -> Unit) {
        val exist = viewModel?.checkWordExisting(value)
        activity?.let {
            exist.observe(it, Observer { flag ->
                if (flag) {
                    viewModel?.currentWord.observe(it, {
                        answer ->
                        val num_cows = value.toList().stream()
                            .filter{letter -> answer?.value?.contains(letter) == true }
                            .count()
                        val num_bulls = value.toList().stream()
                            .filter{letter -> letter == answer?.value?.get(value.indexOf(letter)) ?: false }
                            .count()
                        val w = Word(value, Difficulty.SIMPLE.name, num_bulls.toInt(), num_cows.toInt())
                        call(w)
                    })
                }
                else {
                    Toast.makeText(activity, "Я не знаю такого слова...", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}