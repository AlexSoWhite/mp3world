package com.nafanya.mp3world.features.playerView.view

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.features.downloading.DownloadInteractor
import com.nafanya.mp3world.features.downloading.DownloadingViewModel
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

/**
 * ViewModel for player control views (FullScreen and Bottom)
 */
class PlayerViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
    override val downloadInteractor: DownloadInteractor,
    override val mediaStoreInteractor: MediaStoreInteractor
) : ViewModel(), DownloadingViewModel {

    val isPlayerInitialised = playerInteractor.isPlayerInitialised
    val player = playerInteractor.player
    val currentSong = playerInteractor.currentSong
    val currentPlaylist = playerInteractor.currentPlaylist
}
