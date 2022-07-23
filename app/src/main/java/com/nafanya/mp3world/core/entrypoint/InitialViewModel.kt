package com.nafanya.mp3world.core.entrypoint

import androidx.lifecycle.*
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

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
