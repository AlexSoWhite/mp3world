package com.nafanya.mp3world.presentation.player_view

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.nafanya.mp3world.core.utils.ColorExtractor
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.data.downloading.api.DownloadInteractor
import com.nafanya.mp3world.data.downloading.api.DownloadingViewModel
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.domain.favourites.FavouritesManager
import com.nafanya.player.interactor.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel for player control views (FullScreen and Bottom)
 */
class PlayerViewModel @Inject constructor(
    private val downloadInteractor: DownloadInteractor,
    private val favouriteListManager: FavouritesProvider,
    private val colorExtractor: ColorExtractor,
    private val playerInteractor: PlayerInteractor
) : ViewModel(), DownloadingViewModel, FavouritesManager {

    val isPlayerReady = playerInteractor.isPlayerReady
    val player: Player? get() = playerInteractor.player
    val currentSong = playerInteractor.currentSong

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

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun getAverageColorWithNoWhiteComponent(art: Bitmap) = colorExtractor.getAverageColorWithNoWhiteComponent(art)
}
