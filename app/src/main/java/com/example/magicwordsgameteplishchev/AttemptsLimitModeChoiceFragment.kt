package com.example.magicwordsgameteplishchev

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import com.google.android.material.slider.Slider
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AttemptsLimitModeChoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttemptsLimitModeChoiceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_attempts_limit_mode_choice, container, false)

        val slider = view.findViewById<Slider>(R.id.attempts_limit_mode_slider)
        var numberAttempts = 1
        slider.addOnChangeListener { slider, value, fromUser ->
            numberAttempts = value.roundToInt()
        }
        val radioBtnSimple = view.findViewById<RadioButton>(R.id.choose_classic_mode_dif_simple)
        val radioBtnHard = view.findViewById<RadioButton>(R.id.choose_classic_mode_dif_hard)
        val radioBtnNum3 = view.findViewById<RadioButton>(R.id.choose_classic_mode_num_letters_3)
        val radioBtnNum4 = view.findViewById<RadioButton>(R.id.choose_classic_mode_num_letters_4)
        val radioBtnNum5 = view.findViewById<RadioButton>(R.id.choose_classic_mode_num_letters_5)
        val btnStart = view.findViewById<Button>(R.id.choose_classic_mode_btn_start)

        btnStart.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            var difficulty = ""
            if (radioBtnHard.isChecked) {
                difficulty = "H"
            }
            else {
                difficulty = "S"
            }
            var num_letters = 0

            if (radioBtnNum3.isChecked) {
                num_letters = 3
            } else if (radioBtnNum4.isChecked) {
                num_letters = 4
            } else {
                num_letters = 5
            }

            intent.putExtra("difficulty", difficulty)
            intent.putExtra("num_letters", num_letters)
            intent.putExtra("mode", "limit_attempts")
            intent.putExtra("num_attempts", numberAttempts)
            startActivity(intent)
        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AttemptsLimitModeChoiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AttemptsLimitModeChoiceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}