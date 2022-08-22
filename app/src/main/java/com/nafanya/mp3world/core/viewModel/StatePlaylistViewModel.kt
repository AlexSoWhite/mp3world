package com.nafanya.mp3world.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Song
import kotlinx.coroutines.flow.combine

/**
 * ViewModel for.
 */
abstract class StatePlaylistViewModel(
    private val playerInteractor: PlayerInteractor,
    val playlist: LiveData<PlaylistWrapper?>
) : StateMachine<SongWrapper, SongListItem>(
    playlist.map { it?.songList }
),
    PlaylistViewModel {

    final override val isPlaying = playerInteractor.isPlaying
    final override val isFirstItemBound = MutableLiveData(false)

    final override val currentSong = combine(
        playerInteractor.currentSong.map { it as SongWrapper }.asFlow(),
        isFirstItemBound.asFlow()
    ) { song, _ ->
        song
    }.asLiveData()

    init {
        resetContainerSizeSource(playlist.map { it?.songList?.size ?: 0 })
        compositeObservable.addObserver(playlist) {
            resetInitialTitle(it?.name ?: "")
        }
    }

    final override fun onSongClick(song: Song) {
        playlist.value?.let {
            playerInteractor.setPlaylist(it)
            playerInteractor.setSong(song)
        }
    }

    final override fun onFirstItemBound() {
        isFirstItemBound.value = true
    }
}
