package com.nafanya.mp3world.features.remoteSongs.di

import com.nafanya.mp3world.features.remoteSongs.QueryExecutor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class RemoteSongsModule {

    @Provides
    fun provideQueryExecutor(): QueryExecutor {
        return QueryExecutor(OkHttpClient())
    }
}
