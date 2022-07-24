package com.nafanya.mp3world.features.downloading

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.domain.Song
import javax.inject.Inject

class DownloadViewModel @Inject constructor(
    var downloader: Downloader
) : ViewModel() {

    fun download(song: Song, callback: (DownloadResult) -> Unit) {
        downloader.download(song, callback)
    }
}
