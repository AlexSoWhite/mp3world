package com.nafanya.player

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.IllegalSeekPositionException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.audio.AudioAttributes
import com.nafanya.player.Listener.Companion.URI_KEY

internal class AoedePlayer(context: Context) {

    /**
     * player itself
     */
    internal var player: AoedePlayerWrapper? = null

    /**
     * sets to true when first mediaItem is loaded
     */
    private var isInitialized = false

    /**
     * current playlist, to navigate between items in it
     */
    private val mCurrentPlaylist = MutableLiveData<Playlist>()
    private val mPlaylist: Playlist?
        get() = mCurrentPlaylist.value
    internal val currentPlaylist: LiveData<Playlist?>
        get() = mCurrentPlaylist

    init {
        player = AoedePlayerWrapper(
            ExoPlayer.Builder(context).build()
        )
        player?.exoPlayer?.apply {
            setHandleAudioBecomingNoisy(true)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
            setAudioAttributes(audioAttributes, true)
        }
    }

    internal fun addListener(listener: Listener) {
        player?.addListener(listener)
    }

    /**
     * Resets player playlist. Must be called before resetting a song.
     */
    internal fun setPlaylist(playlist: Playlist) {
        // creating copy of playlist to continue playing deleted from playlist songs
        // TODO for what?
        player?.clearMediaItems()
        mCurrentPlaylist.value = playlist
        mPlaylist?.songList?.forEach {
            player?.addMediaItem(it.toMediaItem())
        }
        if (isInitialized) {
            player?.prepare()
        } else {
            isInitialized = true
        }
    }

    /**
     * Resets player song.
     * @param song must exist in current player playlist.
     * @throws IllegalSeekPositionException if song isn't a member of playlist
     */
    internal fun setSong(song: Song) {
        val idx = mPlaylist?.songList?.indexOf(song)
        try {
            if (idx != null) {
                player?.seekToDefaultPosition(idx)
                player?.play()
            }
        } catch (e: IllegalSeekPositionException) {
            throw e
        }
    }

    private fun Song.toMediaItem(): MediaItem {
        val extras = Bundle()
        extras.putParcelable(URI_KEY, this.uri)
        return MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setExtras(extras)
                    .build()
            )
            .build()
    }

    internal fun destroy() {
        player?.release()
        player = null
    }
}
