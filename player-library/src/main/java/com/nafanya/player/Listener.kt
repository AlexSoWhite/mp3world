package com.nafanya.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

/**
 TODO: statistic
 */
internal class Listener(
    private val playerInteractor: PlayerInteractor
) : Player.Listener {

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

    private val _currentSong: MutableLiveData<Song> by lazy {
        MutableLiveData<Song>()
    }
    internal val currentSong: LiveData<Song>
        get() = _currentSong

    private val _isPlaying: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    internal val isPlaying: LiveData<Boolean>
        get() = _isPlaying

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

    /**
     * search song in current playlist to save image
     */
    private fun postLocalSong(mediaItem: MediaItem) {
        _currentSong.value = playerInteractor.currentPlaylist.value?.songList?.first {
            it.id == mediaItem.mediaMetadata.extras!!.getLong("id")
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
        _currentSong.value = song
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
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
