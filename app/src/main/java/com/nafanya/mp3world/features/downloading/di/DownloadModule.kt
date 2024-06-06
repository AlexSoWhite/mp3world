package com.nafanya.mp3world.features.downloading.di

import android.content.Context
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.MainCoroutineProvider
import com.nafanya.mp3world.features.downloading.api.DownloadInteractor
import com.nafanya.mp3world.features.downloading.internal.DownloadInteractorImpl
import com.nafanya.mp3world.features.downloading.internal.DownloadManagerInteractor
import com.nafanya.mp3world.features.downloading.internal.Downloader
import dagger.Module
import dagger.Provides

@Module
class DownloadModule {

    @Provides
    fun provideDownloadInteractor(
        context: Context,
        ioCoroutineProvider: IOCoroutineProvider,
        mainCoroutineProvider: MainCoroutineProvider
    ): DownloadInteractor {
        val downloadManagerInteractor = DownloadManagerInteractor(context)
        val downloader = Downloader(context, downloadManagerInteractor)
        return DownloadInteractorImpl(downloader, ioCoroutineProvider, mainCoroutineProvider)
    }
}
