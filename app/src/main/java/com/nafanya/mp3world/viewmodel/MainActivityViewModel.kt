package com.nafanya.mp3world.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.localStorage.DatabaseInitializer
import com.nafanya.mp3world.model.wrappers.Playlist
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivityViewModel : ViewModel() {

    fun initialize(context: Context) {
        viewModelScope.launch {
            SongListManager.initializeSongList(context)
            thread {
                DatabaseInitializer.init(context)
                PlaylistListManager.initialize()
            }
            ForegroundServiceLiveDataProvider.currentPlaylist.value =
                Playlist(SongListManager.getSongList())
        }
    }
}
