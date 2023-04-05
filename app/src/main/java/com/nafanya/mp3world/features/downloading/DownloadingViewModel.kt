package com.nafanya.mp3world.features.downloading

import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong

/**
 * ViewModel for views that can trigger downloading
 */
interface DownloadingViewModel {

    val downloadInteractor: DownloadInteractor

    val mediaStoreInteractor: MediaStoreInteractor

    fun download(remoteSong: RemoteSong, callback: (DownloadResult) -> Unit) {
        downloadInteractor.download(remoteSong) {
            callback(it)
        }
    }

    fun resetMediaStore() {
        mediaStoreInteractor.reset()
    }
}
