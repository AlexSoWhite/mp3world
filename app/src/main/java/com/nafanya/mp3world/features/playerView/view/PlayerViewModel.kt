package com.nafanya.mp3world.features.playerView.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.features.downloading.api.DownloadInteractor
import com.nafanya.mp3world.features.downloading.api.DownloadResult
import com.nafanya.mp3world.features.downloading.api.DownloadingViewModel
import com.nafanya.mp3world.features.favourites.FavouritesManager
import com.nafanya.mp3world.features.favourites.FavouritesManagerProxy
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel for player control views (FullScreen and Bottom)
 */
class PlayerViewModel @Inject constructor(
    private val downloadInteractor: DownloadInteractor,
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val favouriteListManager: FavouritesManager,
    playerInteractor: PlayerInteractor
) : ViewModel(), DownloadingViewModel, FavouritesManagerProxy {

    val isPlayerInitialised = playerInteractor.isPlayerInitialised
    val player = playerInteractor.player
    val currentSong = playerInteractor.currentSong
    val currentPlaylist = playerInteractor.currentPlaylist

    override fun isSongInFavourites(song: LocalSong) = favouriteListManager.isSongInFavourites(song)

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

    override fun download(remoteSong: RemoteSong, callback: (DownloadResult) -> Unit) {
        downloadInteractor.download(remoteSong, callback)
    }

    override fun resetMediaStore() {
        mediaStoreInteractor.reset()
    }
}
