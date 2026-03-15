package com.nafanya.player.aoede_player

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.nafanya.player.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * TODO: statistic
 */
internal class PlayerListener(
    private val mediaItemConverter: MediaItemConverter
) : Player.Listener {

    companion object {
        private const val TAG = "_PlayerListener"
    }

    private val _isPlaying = MutableStateFlow(false)
    internal val isPlaying: StateFlow<Boolean> get() = _isPlaying.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    internal val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private fun Int.toMediaItemTransitionReason(): String {
        return when (this) {
            Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT -> "repeat"
            Player.MEDIA_ITEM_TRANSITION_REASON_AUTO -> "auto"
            Player.MEDIA_ITEM_TRANSITION_REASON_SEEK -> "seek"
            Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED -> "playlist changed"
            else -> throw IllegalStateException("Unknown media item transition reason: $this")
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            val song = mediaItemConverter.getSongFromMediaItem(it)
            Log.d(TAG, "onMediaItemTransition, reason: ${reason.toMediaItemTransitionReason()}, transitionedTo: $song")
            _currentSong.value = song
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.d(TAG, "onIsPlayingChanged isPlaying: $isPlaying")
        _isPlaying.value = isPlaying
    }
}
