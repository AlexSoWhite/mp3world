package com.nafanya.mp3world.features.mediaStore.di

import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractorImpl
import com.nafanya.mp3world.features.mediaStore.MediaStoreReader
import com.nafanya.mp3world.features.mediaStore.MediaStoreReaderImpl
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
