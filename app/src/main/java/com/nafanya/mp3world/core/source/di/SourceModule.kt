package com.nafanya.mp3world.core.source.di

import com.nafanya.mp3world.core.source.SourceProvider
import com.nafanya.player.Playlist
import dagger.Module
import dagger.Provides

// TODO consider refactoring
@Module
class SourceModule {

    @Provides
    fun providePlaylist(): com.nafanya.player.Playlist {
        return SourceProvider.takePlaylist()!!
    }

    @Provides
    fun provideQuery(): String {
        return SourceProvider.takeQuery()!!
    }
}
