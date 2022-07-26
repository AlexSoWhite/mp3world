package com.nafanya.mp3world.features.playerView.view

import androidx.lifecycle.ViewModel
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    val isPlayerInitialised
        get() = playerInteractor.isPlayerInitialised
    val player
        get() = playerInteractor.player
    val currentSong
        get() = playerInteractor.currentSong
}
