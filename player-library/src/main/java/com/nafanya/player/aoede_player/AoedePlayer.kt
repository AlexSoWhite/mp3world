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
import kotlinx.coroutines.flow.asStateFlow

@UnstableApi
internal class AoedePlayer(
    context: Context,
    mediaItemConverter: MediaItemConverter
) {

    private companion object {
        const val TAG = "_AoedePlayer"
    }


    /**
     * Object that connects [AoedePlayer] with public flows.
     */
    private val playerListener: PlayerListener = PlayerListener(mediaItemConverter)

    internal val isPlaying = playerListener.isPlaying

    internal val currentSong = playerListener.currentSong

    /**
     * Extended player with custom behavior.
     */
    private val _player: ExoPlayerWrapper = ExoPlayerWrapper(
        ExoPlayer.Builder(context).build()
    ).apply { this.addListener(playerListener) }

    internal val player: ExoPlayerWrapper get() = _player

    /**
     * current playlist, to navigate between items in it
     */
    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)
    internal val currentPlaylist: StateFlow<Playlist?> get() = _currentPlaylist.asStateFlow()

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
        Log.d(TAG, "setPlaylist: $playlist")
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
        Log.d(TAG, "setSong: $song")
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
}
