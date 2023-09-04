package com.nafanya.mp3world.features.downloading

import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor

/**
 * ViewModel for views that can trigger downloading
 *
 * TODO: make it actual interface
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
