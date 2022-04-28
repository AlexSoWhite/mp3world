package com.nafanya.mp3world.model.dependencies

import android.content.Context
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.network.MetadataScanner
import com.nafanya.mp3world.model.network.QueryExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideDownloader(@ApplicationContext applicationContext: Context): Downloader {
        return Downloader(applicationContext)
    }

    @Provides
    fun provideQueryExecutor(): QueryExecutor {
        return QueryExecutor(OkHttpClient())
    }
}
