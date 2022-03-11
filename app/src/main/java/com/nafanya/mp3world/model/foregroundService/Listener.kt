package com.nafanya.mp3world.model.foregroundService

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Song

object Listener : Player.Listener {

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            if (it.mediaMetadata.extras!!.getString("url") != null) {
                postUrlBasedSong(it)
            } else {
                postLocalSong(it)
            }
        }
    }

    private fun postUrlBasedSong(it: MediaItem) {
        val song = Song(
            id = it.mediaMetadata.extras!!.getLong("id"),
            title = it.mediaMetadata.extras!!.getString("title"),
            artist = it.mediaMetadata.extras!!.getString("artist"),
            date = it.mediaMetadata.extras!!.getString("date"),
            url = it.mediaMetadata.extras!!.getString("url"),
            duration = null,
            path = it.mediaMetadata.extras!!.getString("path")
        )
        ForegroundServiceLiveDataProvider.currentSong.value = song
    }

    private fun postLocalSong(it: MediaItem) {
        SongListManager.songList.value?.forEach { elem ->
            if (elem.id == it.mediaMetadata.extras!!.getLong("id")) {
                ForegroundServiceLiveDataProvider.currentSong.value = elem
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        ForegroundServiceLiveDataProvider.isPlaying.value = isPlaying
    }
}
