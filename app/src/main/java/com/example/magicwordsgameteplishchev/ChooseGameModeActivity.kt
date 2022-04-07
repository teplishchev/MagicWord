package com.example.magicwordsgameteplishchev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class ChooseGameModeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_game_mode)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.choose_game_mode_bottom_navigation)
        changeFragment(ClassicModeChoiceFragment())

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_choose_mode_classic ->
                    changeFragment(ClassicModeChoiceFragment())
                R.id.menu_choose_mode_limit_time ->
                    changeFragment(ClassicModeChoiceFragment())
                R.id.menu_choose_mode_limit_attempts ->
                    changeFragment(AttemptsLimitModeChoiceFragment())
            }
            true
        }

    }

    private fun changeFragment(fragment: Fragment) {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.choose_game_mode_fragment_container, fragment)
                .commit()
        }
    }

}