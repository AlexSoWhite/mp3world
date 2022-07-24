package com.nafanya.mp3world.features.foregroundService

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.nafanya.mp3world.core.domain.Song
import com.nafanya.mp3world.features.allSongs.SongListManager

/**
 TODO: statistic
 */
class Listener : Player.Listener {

    // statistic
//    private var startPlayingSongTime: Date? = null
//    private var endPlayingSongTime: Date? = null
//    private var playingTime: Long? = null
//    private var previousSong: Song? = null
//
//    companion object {
//        // 5 seconds
//        private const val addingSongToStatisticEntityThreshold = 5000
//    }

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
            date = it.mediaMetadata.extras!!.getLong("date"),
            url = it.mediaMetadata.extras!!.getString("url"),
            duration = it.mediaMetadata.extras!!.getLong("duration"),
            artUrl = it.mediaMetadata.extras!!.getString("artUrl")
        )
        PlayerLiveDataProvider.currentSong.value = song
    }

    private fun postLocalSong(it: MediaItem) {
        SongListManager.songList.value?.forEach { elem ->
            if (elem.id == it.mediaMetadata.extras!!.getLong("id")) {
                PlayerLiveDataProvider.currentSong.value = elem
                // logStatistic()
                // previousSong = elem
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        PlayerLiveDataProvider.isPlaying.value = isPlaying
//        if (!isPlaying) {
//            playingTime = Date().time - startPlayingSongTime!!.time
//            startPlayingSongTime?.time = 0
//        } else {
//            startPlayingSongTime = Date()
//        }
    }

    fun destroy() {
        // logStatistic()
    }

//    @Suppress("NestedBlockDepth")
//    private fun logStatistic() {
//        endPlayingSongTime = Date()
//        startPlayingSongTime?.let {
//            if (startPlayingSongTime!!.time > 0 && playingTime != null) {
//                playingTime = playingTime!! +
//                    endPlayingSongTime!!.time -
//                    startPlayingSongTime!!.time
//            } else if (startPlayingSongTime!!.time > 0) {
//                playingTime = endPlayingSongTime!!.time - startPlayingSongTime!!.time
//            }
//            var entity: SongStatisticEntity? = null
//            StatisticInfoManager.info.value!!.forEach { e ->
//                if (e.id == previousSong!!.id) {
//                    entity = e
//                    e.time?.let {
//                        playingTime = playingTime!! + it
//                    }
//                }
//            }
//            if (playingTime!! >= addingSongToStatisticEntityThreshold) {
//                when (entity) {
//                    null -> {
//                        LocalStorageProvider.addStatisticEntity(
//                            context,
//                            SongStatisticEntity(
//                                previousSong!!.id,
//                                playingTime,
//                                previousSong!!.title,
//                                previousSong!!.artist
//                            )
//                        )
//                    }
//                    else -> {
//                        LocalStorageProvider.updateStatisticEntity(
//                            context,
//                            SongStatisticEntity(
//                                previousSong!!.id,
//                                playingTime,
//                                previousSong!!.title,
//                                previousSong!!.artist
//                            )
//                        )
//                    }
//                }
//            }
//        }
//        startPlayingSongTime = Date()
//    }
}
