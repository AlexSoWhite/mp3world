package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    fun initializeLists() {
        if (!isInitialized) {
            viewModelScope.launch {
                isInitialized = true
                // initialize songList
                MediaStoreReader().readMediaStore()
                LocalStorageProvider().populateLists()
            }
        }
    }

    companion object {
        private var isInitialized = false
    }
}
