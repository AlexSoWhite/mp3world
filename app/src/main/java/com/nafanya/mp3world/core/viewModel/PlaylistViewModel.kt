package com.nafanya.mp3world.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.player.Song

interface PlaylistViewModel {

    val isPlaying: LiveData<Boolean>

    val currentSong: LiveData<SongWrapper>

    val isFirstItemBound: MutableLiveData<Boolean>

    fun onFirstItemBound()

    fun onSongClick(song: Song)
}
