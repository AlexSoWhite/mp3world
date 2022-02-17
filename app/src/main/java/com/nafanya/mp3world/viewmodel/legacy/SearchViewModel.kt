package com.nafanya.mp3world.viewmodel.legacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Playlist
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    fun download(name: String, callback: (Playlist) -> Unit) {
        viewModelScope.launch {
            Downloader.preLoad(name) {
                it?.let { playlist ->
                    callback(playlist)
                }
            }
        }
    }
}
