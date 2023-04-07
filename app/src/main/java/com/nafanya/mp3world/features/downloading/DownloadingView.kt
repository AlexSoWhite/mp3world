package com.nafanya.mp3world.features.downloading

import android.content.Context
import com.nafanya.mp3world.core.utils.showToast
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong

/**
 * Interface for views that can trigger downloading
 */
interface DownloadingView {

    val downloadingViewModel: DownloadingViewModel

    fun download(context: Context, song: RemoteSong) {
        context.showToast("загрузка начата")
        downloadingViewModel.download(song) {
            when (it.type) {
                ResultType.SUCCESS -> {
                    context.showToast("${song.artist} - ${song.title} загружено")
                    downloadingViewModel.resetMediaStore()
                }
                ResultType.ERROR -> context.showToast("ошибка")
            }
        }
    }
}
