package com.nafanya.mp3world.features.downloading

import androidx.lifecycle.ViewModel
import com.nafanya.player.Song
import javax.inject.Inject

class DownloadViewModel @Inject constructor(
    var downloader: Downloader
) : ViewModel() {

    fun download(song: Song, callback: (DownloadResult) -> Unit) {
        downloader.download(song, callback)
    }
}
