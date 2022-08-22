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

    companion object {
        const val URI_KEY = "uri"
    }

    private val _currentSong = MutableLiveData<Song>()
    internal val currentSong: LiveData<Song>
        get() = _currentSong

    private val _isPlaying = MutableLiveData(false)
    internal val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            _currentSong.value = playerInteractor.currentPlaylist.value?.songList?.first {
                it.uri == mediaItem.mediaMetadata.extras!!.getParcelable(URI_KEY)
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    fun destroy() {
        // logStatistic()
    }
}
