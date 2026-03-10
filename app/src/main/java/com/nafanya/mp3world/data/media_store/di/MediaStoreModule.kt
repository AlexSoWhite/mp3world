package com.nafanya.mp3world.data.media_store.di

import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import com.nafanya.mp3world.data.media_store.MediaStoreInteractorImpl
import com.nafanya.mp3world.data.media_store.MediaStoreReader
import com.nafanya.mp3world.data.media_store.MediaStoreReaderImpl
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
