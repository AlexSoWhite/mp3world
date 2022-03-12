package com.nafanya.mp3world.view.playerViews

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import kotlin.concurrent.thread

open class GenericPlayerControlView(
    private val activity: AppCompatActivity,
    layoutResId: Int
) {
    var playerControlView: StyledPlayerControlView = activity.findViewById(layoutResId)

    init {
        // setting view
        playerControlView.showTimeoutMs = 0
        playerControlView.isNestedScrollingEnabled = false
        // setting player
        playerControlView.player = ForegroundServiceLiveDataProvider.getPlayer()
        playerControlView.repeatToggleModes =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
        // setting song observer
        setSongObserver()
    }

    private fun setSongObserver() {
        // observe current song state
        val songObserver = Observer<Song> { song ->
            activity.findViewById<TextView>(R.id.track_title).text = song.title
            activity.findViewById<TextView>(R.id.track_artist).text = song.artist
            thread {
                if (song.art != null) {
                    activity.findViewById<ImageView>(
                        R.id.control_song_icon
                    ).setImageBitmap(song.art)
                } else {
                    activity.findViewById<ImageView>(R.id.control_song_icon)
                        .setImageResource(R.drawable.default_placeholder)
                }
            }
        }
        ForegroundServiceLiveDataProvider.currentSong.observe(activity, songObserver)
    }

    fun setPlaylistObserver(callback: (Playlist) -> Unit) {
        // observe current playlist state
        val playlistObserver = Observer<Playlist> {
            callback(it)
        }
        ForegroundServiceLiveDataProvider.currentPlaylist.observe(activity, playlistObserver)
    }
}
