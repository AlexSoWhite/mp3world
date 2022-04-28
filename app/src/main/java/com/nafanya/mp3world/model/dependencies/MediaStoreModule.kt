package com.nafanya.mp3world.model.dependencies

import android.content.Context
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MediaStoreModule {

    @Provides
    fun providesMediaStoreReader(@ApplicationContext context: Context): MediaStoreReader {
        return MediaStoreReader.Builder().withContext(context).build()
    }
}
