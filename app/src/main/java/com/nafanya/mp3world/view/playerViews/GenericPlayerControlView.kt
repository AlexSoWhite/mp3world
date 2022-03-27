package com.nafanya.mp3world.view.playerViews

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Song

open class GenericPlayerControlView(
    private val activity: AppCompatActivity,
    layoutResId: Int
) {
    // id in activity xml
    var playerControlView: StyledPlayerControlView = activity.findViewById(layoutResId)

    init {
        // setting view
        playerControlView.showTimeoutMs = 0
        playerControlView.isNestedScrollingEnabled = false
        // setting player
        val playerObserver = Observer<Boolean> {
            if (it) {
                playerControlView.player = PlayerLiveDataProvider.getPlayer()
            }
        }
        PlayerLiveDataProvider.isPlayerInitialized.observe(activity, playerObserver)
        playerControlView.repeatToggleModes =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
    }

    open fun setSongObserver() {
        // observe current song state
        val songObserver = Observer<Song> { song ->
            activity.findViewById<LinearLayout>(R.id.controls_view).visibility = View.VISIBLE
            activity.findViewById<TextView>(R.id.track_title).text = song.title
            activity.findViewById<TextView>(R.id.track_artist).text = song.artist
            val songIcon = activity.findViewById<ImageView>(R.id.control_song_icon)
            when {
                song.art != null -> {
                    songIcon.setImageBitmap(song.art)
                }
                song.artUrl != null -> {
                    Glide.with(songIcon)
                        .load(song.artUrl)
                        .into(songIcon)
                }
                else -> {
                    songIcon.setImageResource(R.drawable.default_placeholder)
                }
            }
        }
        PlayerLiveDataProvider.currentSong.observe(activity, songObserver)
    }
}
