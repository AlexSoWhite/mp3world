package com.nafanya.mp3world.core.mediaStore.di

import android.content.Context
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import dagger.Module
import dagger.Provides

@Module
class MediaStoreModule {

    @Provides
    fun providesMediaStoreReader(context: Context): MediaStoreReader {
        return MediaStoreReader.Builder().withContext(context).build()
    }
}
