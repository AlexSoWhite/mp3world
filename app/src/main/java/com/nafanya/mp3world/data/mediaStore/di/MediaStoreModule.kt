package com.nafanya.mp3world.data.mediaStore.di

import com.nafanya.mp3world.data.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.data.mediaStore.MediaStoreInteractorImpl
import com.nafanya.mp3world.data.mediaStore.MediaStoreReader
import com.nafanya.mp3world.data.mediaStore.MediaStoreReaderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface MediaStoreModule {

    @Binds
    @Singleton
    fun mediaStoreReader(impl: MediaStoreReaderImpl): MediaStoreReader

    @Binds
    @Singleton
    fun mediaStoreInteractor(interactorImpl: MediaStoreInteractorImpl): MediaStoreInteractor
}
