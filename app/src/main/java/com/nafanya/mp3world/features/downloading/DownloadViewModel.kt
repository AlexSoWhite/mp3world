package com.nafanya.mp3world.features.downloading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import javax.inject.Inject
import kotlinx.coroutines.launch

class DownloadViewModel @Inject constructor(
    private val downloader: Downloader,
) : ViewModel() {

    fun download(song: RemoteSong, callback: (DownloadResult) -> Unit) {
        viewModelScope.launch {
            downloader.download(song) {
                callback(it)
            }
        }
    }
}
