package com.nafanya.mp3world.core.di

import android.content.Context
import com.nafanya.mp3world.core.entrypoint.di.EntrypointComponentProvider
import com.nafanya.mp3world.core.mediaStore.di.MediaStoreModule
import com.nafanya.mp3world.core.source.di.SourceModule
import com.nafanya.mp3world.features.albums.di.AlbumComponentProvider
import com.nafanya.mp3world.features.artists.di.ArtistsComponentProvider
import com.nafanya.mp3world.features.downloading.di.DownloadModule
import com.nafanya.mp3world.features.favorites.di.FavouritesComponentProvider
import com.nafanya.mp3world.features.foregroundService.di.ForegroundServiceComponentProvider
import com.nafanya.mp3world.features.localStorage.di.DatabaseModule
import com.nafanya.mp3world.features.playerView.di.PlayerViewComponentProvider
import com.nafanya.mp3world.features.playlists.playlist.di.PlaylistComponentProvider
import com.nafanya.mp3world.features.playlists.playlist.di.PlaylistModule
import com.nafanya.mp3world.features.playlists.playlistsList.di.PlaylistListComponentProvider
import com.nafanya.mp3world.features.remoteSongs.di.RemoteSongsModule
import com.nafanya.mp3world.features.searching.di.SearchSongsComponentProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        MediaStoreModule::class,
        DatabaseModule::class,
        RemoteSongsModule::class,
        SourceModule::class,
        DownloadModule::class,
        PlaylistModule::class,
        PlayerModule::class
    ]
)
@Singleton
interface ApplicationComponent :
    EntrypointComponentProvider,
    AlbumComponentProvider,
    ArtistsComponentProvider,
    FavouritesComponentProvider,
    PlaylistComponentProvider,
    PlaylistListComponentProvider,
    PlayerViewComponentProvider,
    SearchSongsComponentProvider,
    ForegroundServiceComponentProvider {

    fun context(): Context

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ApplicationComponent
    }
}
