package com.nafanya.mp3world.model

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.nafanya.mp3world.viewmodel.MainActivityViewModel

object Listener : Player.Listener {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    fun setViewModel(mainActivityViewModel: MainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        val song = PlayerManager.getSong()
        PlayerManager.currentIdx++
        mainActivityViewModel.currentSong.value = song
    }
}
