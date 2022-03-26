package com.nafanya.mp3world.viewmodel

import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    fun initializeLists(context: Context, contentResolver: ContentResolver) {
        if (!isInitialized) {
            viewModelScope.launch {
                isInitialized = true
                // initialize songList
                MediaStoreReader.initializeSongList(context, contentResolver)
                LocalStorageProvider.populateLists(context)
            }
        }
    }

    companion object {
        private var isInitialized = false
    }
}
