package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val mediaStoreReader: MediaStoreReader,
    private val localStorageProvider: LocalStorageProvider
) : ViewModel() {

    fun initializeLists() {
        if (!isInitialized) {
            isInitialized = true
            // initialize songList
            mediaStoreReader.readMediaStore()
            localStorageProvider.populateLists()
        }
    }

    companion object {
        private var isInitialized = false
    }
}
