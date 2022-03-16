package com.nafanya.mp3world.model.foregroundService

import android.content.Context
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.listManagers.StatisticInfoManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity
import java.util.Date

class Listener(private val context: Context) : Player.Listener {

    // statistic
    private var startPlayingSongTime: Date? = null
    private var endPlayingSongTime: Date? = null
    private var playingTime: Long? = null
    private var previousSong: Song? = null
    companion object {
        // 5 seconds
        private const val addingSongToStatisticEntityThreshold = 5000
    }

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
        logStatistic()
        previousSong = song
    }

    private fun postLocalSong(it: MediaItem) {
        SongListManager.songList.value?.forEach { elem ->
            if (elem.id == it.mediaMetadata.extras!!.getLong("id")) {
                ForegroundServiceLiveDataProvider.currentSong.value = elem
                previousSong = elem
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        ForegroundServiceLiveDataProvider.isPlaying.value = isPlaying
        if (!isPlaying) {
            playingTime = Date().time - startPlayingSongTime!!.time
            startPlayingSongTime?.time = 0
        } else {
            startPlayingSongTime = Date()
        }
    }

    fun destroy() {
        logStatistic()
    }

    @Suppress("NestedBlockDepth")
    private fun logStatistic() {
        endPlayingSongTime = Date()
        startPlayingSongTime?.let {
            if (startPlayingSongTime!!.time > 0 && playingTime != null) {
                playingTime = playingTime!! +
                    endPlayingSongTime!!.time -
                    startPlayingSongTime!!.time
            } else if (startPlayingSongTime!!.time > 0) {
                playingTime = endPlayingSongTime!!.time - startPlayingSongTime!!.time
            }
            var entity: SongStatisticEntity? = null
            StatisticInfoManager.info.value!!.forEach { e ->
                if (e.id == previousSong!!.id) {
                    entity = e
                    e.time?.let {
                        playingTime = playingTime!! + it
                    }
                }
            }
            if (playingTime!! >= addingSongToStatisticEntityThreshold) {
                when (entity) {
                    null -> {
                        LocalStorageProvider.addStatisticEntity(
                            context,
                            SongStatisticEntity(
                                previousSong!!.id,
                                playingTime,
                                previousSong!!.title,
                                previousSong!!.artist
                            )
                        )
                    }
                    else -> {
                        LocalStorageProvider.updateStatisticEntity(
                            context,
                            SongStatisticEntity(
                                previousSong!!.id,
                                playingTime,
                                previousSong!!.title,
                                previousSong!!.artist
                            )
                        )
                    }
                }
            }
        }
        startPlayingSongTime = Date()
    }
}
