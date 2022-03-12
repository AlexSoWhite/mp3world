package com.nafanya.mp3world.view.playerViews

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.PlayerViewFullscreenBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider

class FullScreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: PlayerViewFullscreenBinding
    private var playerView: GenericPlayerControlView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.player_view_fullscreen)
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView = GenericPlayerControlView(this, R.id.player_control_fullscreen_view)
                playerView!!.setPlaylistObserver { playlist ->
                    // TODO expand playlist
                }
            }
        }
        ForegroundServiceLiveDataProvider.isPlayerInitialized.observe(
            this,
            observerPlayer
        )
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
