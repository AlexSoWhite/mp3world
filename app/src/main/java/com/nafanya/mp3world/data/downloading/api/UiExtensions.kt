package com.nafanya.mp3world.data.downloading.api

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.showToast
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// todo: this should not be organized like that
fun Fragment.download(
    downloadingViewModel: DownloadingViewModel,
    song: RemoteSong
) {
    val applicationContext = requireContext().applicationContext
    applicationContext.showToast(applicationContext.getString(R.string.download_started))
    val resultFlow = downloadingViewModel.download(song)
    lifecycleScope.launch {
        resultFlow.collectLatest {
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
}
