package com.nafanya.mp3world.core.entrypoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import javax.inject.Inject
import kotlinx.coroutines.launch

class InitialViewModel @Inject constructor(
    private val mediaStoreReader: MediaStoreReader,
    private val localStorageProvider: LocalStorageProvider
) : ViewModel() {

    fun initializeLists() {
        viewModelScope.launch {
            if (!isInitialized) {
                isInitialized = true
                // initialize songList
                mediaStoreReader.readMediaStore()
                localStorageProvider.populateLists()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        localStorageProvider.dbHolder.closeDataBase()
    }

    companion object {
        private var isInitialized = false
    }
}
