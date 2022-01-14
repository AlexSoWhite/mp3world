package com.nafanya.mp3world.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.nafanya.mp3world.model.Playlist
import com.nafanya.mp3world.model.Song
import com.nafanya.mp3world.model.SongListManager
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val currentSong: MutableLiveData<Song> by lazy {
        MutableLiveData<Song>()
    }

    fun initialize(context: Context, playerView: StyledPlayerControlView) {
        viewModelScope.launch {
            SongListManager.initializeSongList(context)
            ForegroundServiceLiveDataHolder.currentPlaylist.value = Playlist(SongListManager.getSongList())
        }
    }
}
