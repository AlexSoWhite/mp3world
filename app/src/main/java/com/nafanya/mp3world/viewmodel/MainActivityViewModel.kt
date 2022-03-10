package com.nafanya.mp3world.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.localStorage.DatabaseHolder
import com.nafanya.mp3world.model.wrappers.Playlist
import kotlin.concurrent.thread
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    fun initializeLists(context: Context) {
        if (!isInitialized) {
            viewModelScope.launch {
                isInitialized = true
                // initialize songList
                MediaStoreReader.initializeSongList(context)
                // use initialized songList to initialize player state
                ForegroundServiceLiveDataProvider.currentPlaylist.value =
                    Playlist(SongListManager.songList.value!!)
                thread {
                    val db = DatabaseHolder(context)
                    db.populateLists()
                }
            }
        }
    }

    companion object {
        private var isInitialized = false
    }
}
