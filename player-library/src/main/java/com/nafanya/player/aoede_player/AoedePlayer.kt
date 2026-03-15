package com.nafanya.player.aoede_player

import android.content.Context
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.IllegalSeekPositionException
import androidx.media3.common.Player
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

    private fun Player.seekToSong(song: Song, seekPosition: Long? = null) {
        val songIndex = currentPlaylist.value?.songList?.indexOf(song)!!
        if (seekPosition != null) {
            seekTo(songIndex, seekPosition)
        } else {
            seekToDefaultPosition(songIndex)
        }
    }

    /**
     * Resets player playlist. Must be called before resetting a song.
     */
    internal fun setPlaylist(playlist: Playlist) {
        Log.d(TAG, "setPlaylist: $playlist, currentPlaylist: ${_currentPlaylist.value}")
        if (_currentPlaylist.value?.areSongListsTheSame(playlist) == true) {
            Log.d(TAG, "songLists are the same, do not update the player")
            _currentPlaylist.value = playlist
        } else {
            Log.d(TAG, "songLists are different, reset the player")
            player.clearMediaItems()
            Log.d(TAG, "cleared media items")
            playlist.songList.forEach {
                /**
                 * when this method is called for the first time, it ends with
                 * moving player to initialized state
                 */
                player.addMediaItem(it.toMediaItem())
            }
            Log.d(TAG, "updated player media items")
            _currentPlaylist.value = playlist
            Log.d(TAG, "updated current playlist value")
            player.prepare()
            Log.d(TAG, "player prepared")
        }
    }

    /**
     * Resets player song.
     * @param song must exist in current player playlist.
     * @throws IllegalSeekPositionException if song doesn't appear in playlist.
     */
    internal fun toggleSong(song: Song) {
        Log.d(TAG, "toggleSong: $song, currentSong: ${currentSong.value}")
        if (currentSong.value == song) {
            val isPlaying = isPlaying.value
            Log.d(TAG, "toggleSong: same song, toggling player. isPlaying: $isPlaying")
            if (isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        } else {
            Log.d(TAG, "different song, switching to new one")
            player.seekToSong(song)
            player.play()
        }
    }
}
