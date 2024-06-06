package com.nafanya.mp3world.features.downloading.api

import androidx.fragment.app.Fragment
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.showToast
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong

fun Fragment.download(
    downloadingViewModel: DownloadingViewModel,
    song: RemoteSong
) {
    val applicationContext = requireContext().applicationContext
    applicationContext.showToast(applicationContext.getString(R.string.download_started))
    downloadingViewModel.download(song) {
        when (it.type) {
            ResultType.SUCCESS -> {
                applicationContext.showToast(
                    applicationContext.getString(R.string.download_finished, song.artist, song.title)
                )
                downloadingViewModel.resetMediaStore()
            }
            ResultType.ERROR -> applicationContext.showToast(
                applicationContext.getString(R.string.error)
            )
        }
    }
}
