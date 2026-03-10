package com.nafanya.mp3world.presentation.player_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.data.downloading.api.DownloadInteractor
import com.nafanya.mp3world.data.downloading.api.DownloadingViewModel
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.domain.favourites.FavouritesManager
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel for player control views (FullScreen and Bottom)
 */
class PlayerViewModel @Inject constructor(
    private val downloadInteractor: DownloadInteractor,
    private val favouriteListManager: FavouritesProvider,
    playerInteractor: PlayerInteractor
) : ViewModel(), DownloadingViewModel, FavouritesManager {

    val isPlayerInitialised = playerInteractor.isFirstSongSubmitted
    val player = playerInteractor.player
    val currentSong = playerInteractor.currentSong
    val currentPlaylist = playerInteractor.currentPlaylist

    override fun isSongInFavourites(song: LocalSong) = favouriteListManager.observeIsSongInFavorites(song)

    override fun addFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouriteListManager.add(song)
        }
    }

    override fun deleteFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouriteListManager.delete(song)
        }
    }

    override fun download(remoteSong: RemoteSong) = downloadInteractor.download(remoteSong)
}
