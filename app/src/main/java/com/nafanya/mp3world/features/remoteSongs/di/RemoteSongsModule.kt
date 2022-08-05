package com.nafanya.mp3world.features.remoteSongs.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class RemoteSongsModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}
