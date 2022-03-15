package com.nafanya.mp3world.viewmodel

import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    fun initializeLists(context: Context, contentResolver: ContentResolver) {
        if (!isInitialized) {
            viewModelScope.launch {
                isInitialized = true
                // initialize songList
                MediaStoreReader.initializeSongList(context, contentResolver)
                // use initialized songList to initialize player state
                ForegroundServiceLiveDataProvider.currentPlaylist.value =
                    Playlist(
                        SongListManager.songList.value!!,
                        id = -1,
                        name = "Мои песни"
                    )
                LocalStorageProvider.populateLists(context)
            }
        }
    }

    companion object {
        private var isInitialized = false
    }
}
