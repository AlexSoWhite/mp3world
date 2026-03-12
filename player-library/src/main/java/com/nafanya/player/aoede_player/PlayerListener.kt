package com.nafanya.player.aoede_player

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.nafanya.player.Song

/**
 * TODO: statistic
 */
internal class PlayerListener(
    private val player: AoedePlayer
) : Player.Listener {

    companion object {
        private const val TAG = "_PlayerListener"
    }

    private var onCurrentSongUpdateListener: ((Song) -> Unit)? = null

    private var onIsPlayingChangedListener: ((Boolean) -> Unit)? = null

    internal fun setOnCurrentSongUpdateListener(callback: (Song) -> Unit) {
        onCurrentSongUpdateListener = callback
    }

    internal fun setOnIsPlayingChangeListener(callback: (Boolean) -> Unit) {
        onIsPlayingChangedListener = callback
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Log.d(TAG, "onMediaItemTransition")
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            player.currentPlaylist.value?.songList?.first {
                it.uri == mediaItem.mediaMetadata.extras!!.getParcelable(Song.URI_KEY)
            }?.let { newSong ->
                onCurrentSongUpdateListener?.invoke(newSong)
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.d(TAG, "onIsPlayingChanged isPlaying: $isPlaying")
        onIsPlayingChangedListener?.invoke(isPlaying)
    }
}
