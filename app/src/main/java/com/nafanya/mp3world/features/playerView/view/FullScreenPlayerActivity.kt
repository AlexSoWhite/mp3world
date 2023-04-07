package com.nafanya.mp3world.features.playerView.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityFullScreenPlayerBinding

class FullScreenPlayerActivity : BaseActivity<ActivityFullScreenPlayerBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityFullScreenPlayerBinding {
        return ActivityFullScreenPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        findViewById<ConstraintLayout>(R.id.controls_wrapper).visibility = View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true
        }
        return false
    }
}
