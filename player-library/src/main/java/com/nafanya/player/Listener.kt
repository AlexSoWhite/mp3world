package com.nafanya.player

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

/**
 * TODO: statistic
 */
internal class Listener(
    private val playerInteractor: PlayerInteractor
) : Player.Listener {

    companion object {
        const val URI_KEY = "uri"
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
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            playerInteractor.currentPlaylist.value?.songList?.first {
                it.uri == mediaItem.mediaMetadata.extras!!.getParcelable(URI_KEY)
            }?.let { newSong ->
                onCurrentSongUpdateListener?.invoke(newSong)
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        onIsPlayingChangedListener?.invoke(isPlaying)
    }
}
