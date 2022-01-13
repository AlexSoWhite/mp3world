package com.nafanya.mp3world.model

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.nafanya.mp3world.viewmodel.MainActivityViewModel
import com.nafanya.mp3world.viewmodel.SongListViewModel

object Listener : Player.Listener {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var songListViewModel: SongListViewModel? = null

    fun setMainActivityViewModel(mainActivityViewModel: MainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel
    }

    fun setSongListViewModel(songListViewModel: SongListViewModel) {
        this.songListViewModel = songListViewModel
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            val song = Song(
                it.mediaMetadata.extras!!.getLong("id"),
                it.mediaMetadata.title as String?,
                it.mediaMetadata.artist as String?,
                ""
            )
            mainActivityViewModel.currentSong.value = song
            songListViewModel?.currentSong?.value = song
        }
    }
}
