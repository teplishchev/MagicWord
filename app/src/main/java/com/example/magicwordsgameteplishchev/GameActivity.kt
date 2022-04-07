package com.example.magicwordsgameteplishchev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val mode = intent.getStringExtra("mode") ?: ""
        val numberAttempts = intent.getIntExtra("num_attempts", 1)

        val containerForFragment = findViewById<FrameLayout>(R.id.game_container_for_fragment)
        supportFragmentManager.beginTransaction()
            .add(R.id.game_container_for_fragment, GameFragment.newInstance(mode, numberAttempts)).commit()
    }
}