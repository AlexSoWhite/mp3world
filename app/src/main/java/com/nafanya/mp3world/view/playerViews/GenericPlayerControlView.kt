package com.nafanya.mp3world.view.playerViews

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

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
    }

    fun setSongObserver(callback: (Song) -> Unit) {
        // observe current song state
        val songObserver = Observer<Song> {
            callback(it)
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
