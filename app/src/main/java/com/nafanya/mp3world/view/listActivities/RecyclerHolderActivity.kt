package com.nafanya.mp3world.view.listActivities

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.RecyclerHolderActivityBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.OnSwipeListener

abstract class RecyclerHolderActivity : AppCompatActivity() {

    protected lateinit var binding: RecyclerHolderActivityBinding
    private lateinit var playerView: StyledPlayerControlView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.recycler_holder_activity)

        // top bar appearance and behavior
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        supportActionBar?.title = "${getFragmentDescription()} (${getItemCount()})"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // playerView behavior
        playerView = findViewById(R.id.player_control_view)
        playerView.showTimeoutMs = 0
        playerView.isNestedScrollingEnabled = false
        val observerSong = Observer<Song> {
            findViewById<TextView>(R.id.track_title).text = it.title
            findViewById<TextView>(R.id.track_artist).text = it.artist
        }
        ForegroundServiceLiveDataProvider.currentSong.observe(this, observerSong)
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView.player = ForegroundServiceLiveDataProvider.getPlayer()
                playerView.repeatToggleModes =
                    RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
                    RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
                    RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
            }
        }
        ForegroundServiceLiveDataProvider.isPlayerInitialized.observe(this, observerPlayer)

        // list setting
        setAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        OnSwipeListener(binding.recycler) {
            this.finish()
        }

        // custom options
        addCustomBehavior()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true
        }
        return false
    }

    open fun addCustomBehavior() {}

    // requires child activity to set binding.recycler.adapter
    abstract fun setAdapter()

    abstract fun getItemCount(): Int

    abstract fun getFragmentDescription(): String
}
