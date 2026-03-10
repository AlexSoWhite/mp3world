package com.nafanya.mp3world.data.downloading.di

import android.content.Context
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import com.nafanya.mp3world.data.downloading.api.DownloadInteractor
import com.nafanya.mp3world.data.downloading.internal.DownloadInteractorImpl
import com.nafanya.mp3world.data.downloading.internal.DownloadManagerInteractor
import com.nafanya.mp3world.data.downloading.internal.Downloader
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope

@Module
class DownloadModule {

    @Provides
    fun provideDownloadInteractor(
        applicationContext: Context,
        dispatchersProvider: DispatchersProvider,
        mediaStoreInteractor: MediaStoreInteractor,
        applicationScope: CoroutineScope
    ): DownloadInteractor {
        val downloadManagerInteractor = DownloadManagerInteractor(applicationContext, dispatchersProvider)
        val downloader = Downloader(applicationContext, downloadManagerInteractor)
        return DownloadInteractorImpl(downloader, mediaStoreInteractor, applicationScope)
    }
}
