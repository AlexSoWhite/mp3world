package com.nafanya.mp3world.model.dependencies

import com.nafanya.mp3world.model.wrappers.Playlist
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SourceModule {

    @Provides
    fun providePlaylist(): Playlist {
        return SourceProvider.takePlaylist()!!
    }

    @Provides
    fun provideQuery(): String {
        return SourceProvider.takeQuery()!!
    }
}
