package com.nafanya.mp3world.core.entrypoint

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nafanya.mp3world.R
import java.util.Timer
import java.util.TimerTask

class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    val intent = Intent(this@InitialActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },
            startDelay
        )
    }

    companion object {
        private const val startDelay = 1500L
    }
}
