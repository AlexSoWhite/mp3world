package com.nafanya.mp3world.features.downloading.internal

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.MainCoroutineProvider
import com.nafanya.mp3world.core.coroutines.inScope
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.features.downloading.api.DownloadInteractor
import com.nafanya.mp3world.features.downloading.api.DownloadResult
import kotlinx.coroutines.launch

/**
 * Wrapper class that holds [Downloader] and launches [Downloader.download]
 * in [IOCoroutineProvider.ioScope], handling result in [MainCoroutineProvider.mainScope]
 */
internal class DownloadInteractorImpl(
    private val downloader: Downloader,
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val mainCoroutineProvider: MainCoroutineProvider
) : DownloadInteractor {

    override fun download(song: RemoteSong, callback: (DownloadResult) -> Unit) {
        ioCoroutineProvider.ioScope.launch {
            downloader.download(song) {
                callback.inScope(mainCoroutineProvider.mainScope, it)
            }
        }
    }
}
