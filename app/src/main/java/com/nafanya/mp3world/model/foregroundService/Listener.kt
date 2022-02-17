package com.nafanya.mp3world.model.foregroundService

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.nafanya.mp3world.model.wrappers.Song

object Listener : Player.Listener {

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            val song = Song(
                it.mediaMetadata.extras!!.getLong("id"),
                it.mediaMetadata.title as String?,
                it.mediaMetadata.artist as String?,
                ""
            )
            ForegroundServiceLiveDataProvider.currentSong.value = song
        }
    }
}
