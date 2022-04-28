package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.network.DownloadResult
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    var downloader: Downloader,
    var mediaStoreReader: MediaStoreReader
): ViewModel() {

    fun download(song: Song, callback: (DownloadResult) -> Unit) {
        downloader.download(song, mediaStoreReader, callback)
    }
}
