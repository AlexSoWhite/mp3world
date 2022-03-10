package com.nafanya.mp3world.model.foregroundService

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.nafanya.mp3world.model.wrappers.Song

object Listener : Player.Listener {

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            val song = Song(
                id = it.mediaMetadata.extras!!.getLong("id"),
                title = it.mediaMetadata.title as String?,
                artist = it.mediaMetadata.artist as String?,
                // date = it.mediaMetadata.extras!!.getString("date"),
                url = it.mediaMetadata.extras!!.getString("url")
            )
            ForegroundServiceLiveDataProvider.currentSong.value = song
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        ForegroundServiceLiveDataProvider.isPlaying.value = isPlaying
    }
}
