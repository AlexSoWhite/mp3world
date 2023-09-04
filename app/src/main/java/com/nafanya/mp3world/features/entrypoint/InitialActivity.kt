package com.nafanya.mp3world.features.entrypoint

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.core.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.SplashScreenBinding
import java.util.Timer
import java.util.TimerTask

/**
 * TODO correct splash
*/
class InitialActivity : BaseActivity<SplashScreenBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): SplashScreenBinding {
        return SplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
