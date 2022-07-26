package com.nafanya.player

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes

internal class PlaylistPlayer(context: Context) {

    /**
     * player itself
     */
    internal var player: ExoPlayer? = null

    /**
     * sets to true when first mediaItem is loaded
     */
    private var isInitialized = false

    /**
     * current playlist, to navigate between items in it
     */
    private val _currentPlaylist: MutableLiveData<Playlist?> = MutableLiveData(null)
    private val _playlist: Playlist
        get() = _currentPlaylist.value!!
    internal val currentPlaylist: LiveData<Playlist?>
        get() = _currentPlaylist

    init {
        player = ExoPlayer.Builder(context).build()
        player?.apply {
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
        _currentPlaylist.value = pendingPlaylist
        _playlist.songList.forEach {
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
        val idx = _playlist.songList.indexOf(song)
        try {
            player?.seekToDefaultPosition(idx)
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
