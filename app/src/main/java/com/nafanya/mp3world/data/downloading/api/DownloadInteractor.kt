package com.nafanya.mp3world.data.downloading.api

import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import kotlinx.coroutines.flow.Flow

interface DownloadInteractor {

    fun download(song: RemoteSong): Flow<DownloadResult>
}
