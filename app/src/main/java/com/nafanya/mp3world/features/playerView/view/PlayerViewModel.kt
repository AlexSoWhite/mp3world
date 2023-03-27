package com.nafanya.mp3world.features.playerView.view

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.features.downloading.DownloadInteractor
import com.nafanya.mp3world.features.downloading.DownloadingViewModel
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

/**
 * ViewModel for player control views (FullScreen and Bottom)
 */
class PlayerViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
    override val downloadInteractor: DownloadInteractor,
    override val mediaStoreReader: MediaStoreReader
) : ViewModel(), DownloadingViewModel {

    val isPlayerInitialised = playerInteractor.isPlayerInitialised
    val player = playerInteractor.player
    val currentSong = playerInteractor.currentSong
    val currentPlaylist = playerInteractor.currentPlaylist
}
