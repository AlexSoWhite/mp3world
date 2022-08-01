package com.nafanya.player

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.IllegalSeekPositionException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.audio.AudioAttributes

internal class PlaylistPlayer(context: Context) {

    /**
     * player itself
     */
    internal var player: SmoothlyPausablePlayer? = null

    /**
     * sets to true when first mediaItem is loaded
     */
    private var isInitialized = false

    /**
     * current playlist, to navigate between items in it
     */
    private val mCurrentPlaylist: MutableLiveData<Playlist?> = MutableLiveData(null)
    private val mPlaylist: Playlist
        get() = mCurrentPlaylist.value!!
    internal val currentPlaylist: LiveData<Playlist?>
        get() = mCurrentPlaylist

    init {
        player = SmoothlyPausablePlayer(
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
        val songList = mutableListOf<Song>()
        playlist.songList.forEach {
            songList.add(it)
        }
        val pendingPlaylist = playlist.copy(
            songList = songList
        )
        player?.clearMediaItems()
        mCurrentPlaylist.value = pendingPlaylist
        mPlaylist.songList.forEach {
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
        val idx = mPlaylist.songList.indexOf(song)
        try {
            player?.seekToDefaultPosition(idx)
            player?.play()
        } catch (e: IllegalSeekPositionException) {
            throw e
        }
    }

    private fun Song.toMediaItem(): MediaItem {
        val extras = this.toBundle()
        val uri: Uri =
            this.url?.toUri()
                ?: ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    this.id
                )
        return MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setExtras(extras)
                    .build()
            )
            .build()
    }

    private fun Song.toBundle(): Bundle {
        // creating bundle for listener to provide correct metadata
        val extras = Bundle()
        extras.putLong("id", this.id)
        extras.putString("title", this.title)
        extras.putString("artist", this.artist)
        this.date?.let { it1 -> extras.putLong("date", it1) }
        extras.putString("url", this.url)
        extras.putLong("duration", this.duration!!)
        extras.putString("artUrl", this.artUrl)
        return extras
    }

    internal fun destroy() {
        player?.release()
        player = null
    }
}
