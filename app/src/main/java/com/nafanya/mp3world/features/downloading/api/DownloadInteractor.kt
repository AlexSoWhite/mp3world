package com.nafanya.mp3world.features.downloading.api

import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong

interface DownloadInteractor {

    fun download(song: RemoteSong, callback: (DownloadResult) -> Unit)
}
