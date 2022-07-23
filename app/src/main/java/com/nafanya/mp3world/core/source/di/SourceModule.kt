package com.nafanya.mp3world.core.source.di

import com.nafanya.mp3world.core.source.SourceProvider
import com.nafanya.mp3world.features.playlists.playlist.model.Playlist
import dagger.Module
import dagger.Provides

// TODO consider refactoring
@Module
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
