package com.nafanya.mp3world.data.downloading.internal

import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.data.downloading.api.DownloadInteractor
import com.nafanya.mp3world.data.downloading.api.DownloadResult
import com.nafanya.mp3world.data.downloading.api.ResultType
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Wrapper class that holds [Downloader] and launches [Downloader.download] in [applicationScope]
 */
internal class DownloadInteractorImpl(
    private val downloader: Downloader,
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val applicationScope: CoroutineScope,
) : DownloadInteractor {

    override fun download(song: RemoteSong): Flow<DownloadResult> {
        val resultFlow = MutableSharedFlow<DownloadResult>()
        applicationScope.launch {
            val result = downloader.download(song)
            if (result.type == ResultType.SUCCESS) {
                mediaStoreInteractor.reset()
            }
            resultFlow.emit(result)
        }
        return resultFlow
    }
}
