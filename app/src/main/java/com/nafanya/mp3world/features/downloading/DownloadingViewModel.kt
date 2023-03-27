package com.nafanya.mp3world.features.downloading

import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong

/**
 * ViewModel for views that can trigger downloading
 */
interface DownloadingViewModel {

    val downloadInteractor: DownloadInteractor

    val mediaStoreReader: MediaStoreReader

    fun download(remoteSong: RemoteSong, callback: (DownloadResult) -> Unit) {
        downloadInteractor.download(remoteSong) {
            callback(it)
        }
    }

    fun resetMediaStore() {
        mediaStoreReader.reset()
    }
}
