package com.nafanya.mp3world.features.downloading.api

import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor

/**
 * Interface for view models that can trigger downloading.
 * To implement it use [DownloadInteractor] and [MediaStoreInteractor].
 */
interface DownloadingViewModel {

    fun download(remoteSong: RemoteSong, callback: (DownloadResult) -> Unit)

    fun resetMediaStore()
}
