package com.nafanya.mp3world.features.playerView.view

import androidx.lifecycle.ViewModel
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    playerInteractor: PlayerInteractor
) : ViewModel() {

    val isPlayerInitialised = playerInteractor.isPlayerInitialised
    val player = playerInteractor.player
    val currentSong = playerInteractor.currentSong
    val currentPlaylist = playerInteractor.currentPlaylist
}
