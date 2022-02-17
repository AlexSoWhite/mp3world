package com.nafanya.mp3world.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.listManagers.SongListManager
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    fun initialize(context: Context) {
        viewModelScope.launch {
            SongListManager.initializeSongList(context)
            ForegroundServiceLiveDataProvider.currentPlaylist.value =
                Playlist(SongListManager.getSongList())
        }
    }
}
