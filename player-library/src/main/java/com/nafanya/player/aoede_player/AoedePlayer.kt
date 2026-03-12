package com.nafanya.player.aoede_player

import android.content.Context
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.IllegalSeekPositionException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@UnstableApi
internal class AoedePlayer(context: Context) {

    private companion object {
        const val TAG = "_AoedePlayer"
    }

    private val _isPlaying = MutableStateFlow(false)
    internal val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private val _currentSong = MutableStateFlow<Song?>(null)
    internal val currentSong = _currentSong

    /**
     * Object that connects [AoedePlayer] with public flows.
     */
    private val playerListener: PlayerListener = PlayerListener(this).apply {
        setOnCurrentSongUpdateListener {
            Log.d(TAG, "current song updated: $it")
            _currentSong.value = it
        }
        setOnIsPlayingChangeListener {
            Log.d(TAG, "playback state changed to $it")
            _isPlaying.value = it
        }
    }

    /**
     * Extended player with custom behavior.
     */
    private val _player: ExoPlayerWrapper = ExoPlayerWrapper(
        ExoPlayer.Builder(context).build()
    ).apply { this.addListener(playerListener) }
    internal val player: ExoPlayerWrapper
        get() = _player

    /**
     * current playlist, to navigate between items in it
     */
    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)
    internal val currentPlaylist: StateFlow<Playlist?>
        get() = _currentPlaylist

    init {
        player.exoPlayer.apply {
            setHandleAudioBecomingNoisy(true)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build()
            setAudioAttributes(audioAttributes, true)
        }
    }

    /**
     * Resets player playlist. Must be called before resetting a song.
     */
    internal fun setPlaylist(playlist: Playlist) {
        Log.d(TAG, "setPlaylist")
        // creating copy of playlist to continue playing deleted from playlist songs
        // TODO for what?
        player.clearMediaItems()
        _currentPlaylist.value = playlist
        playlist.songList.forEach {
            /**
             * when this method is called for the first time, it ends with
             * moving player to initialized state
             */
            player.addMediaItem(it.toMediaItem())
        }
        player.prepare()
    }

    /**
     * Resets player song.
     * @param song must exist in current player playlist.
     * @throws IllegalSeekPositionException if song doesn't appear in playlist.
     */
    internal fun setSong(song: Song) {
        Log.d(TAG, "setSong")
        val idx = currentPlaylist.value?.songList?.indexOf(song)
        try {
            if (idx != null) {
                player.seekToDefaultPosition(idx)
                player.play()
            }
        } catch (e: IllegalSeekPositionException) {
            throw e
        }
    }

    internal fun suspend() {
        Log.d(TAG, "suspend")
        player.pause()
    }
}
