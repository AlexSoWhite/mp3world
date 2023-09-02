package com.nafanya.mp3world.core.di

import android.content.Context
import com.google.gson.Gson
import com.nafanya.mp3world.core.wrappers.di.BitmapFlowModelLoaderFactoryComponentProvider
import com.nafanya.mp3world.core.wrappers.di.WrappersModule
import com.nafanya.mp3world.features.albums.di.AlbumComponentProvider
import com.nafanya.mp3world.features.albums.di.AlbumListManagerModule
import com.nafanya.mp3world.features.allPlaylists.di.AllPlaylistsComponentProvider
import com.nafanya.mp3world.features.allPlaylists.di.PlaylistListManagerModule
import com.nafanya.mp3world.features.allSongs.di.AllSongsComponentProvider
import com.nafanya.mp3world.features.allSongs.di.SongListManagerModule
import com.nafanya.mp3world.features.artists.di.ArtistListManagerModule
import com.nafanya.mp3world.features.artists.di.ArtistsComponentProvider
import com.nafanya.mp3world.features.downloading.di.DownloadModule
import com.nafanya.mp3world.features.entrypoint.di.EntrypointComponentProvider
import com.nafanya.mp3world.features.favorites.di.FavouriteListManagerModule
import com.nafanya.mp3world.features.favorites.di.FavouritesComponentProvider
import com.nafanya.mp3world.features.foregroundService.di.ForegroundServiceComponentProvider
import com.nafanya.mp3world.features.mediaStore.di.MediaStoreModule
import com.nafanya.mp3world.features.playerView.di.PlayerViewComponentProvider
import com.nafanya.mp3world.features.playlist.di.PlaylistComponentProvider
import com.nafanya.mp3world.features.remoteSongs.di.RemoteSongsComponentProvider
import com.nafanya.player.PlayerInteractor
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope
import javax.inject.Singleton
import okhttp3.OkHttpClient

@Component(
    modules = [
        ViewModelFactoryModule::class,
        MediaStoreModule::class,
        DownloadModule::class,
        SongListManagerModule::class,
        AlbumListManagerModule::class,
        ArtistListManagerModule::class,
        FavouriteListManagerModule::class,
        PlaylistListManagerModule::class,
        WrappersModule::class
    ]
)
@Singleton
interface ApplicationComponent :
    EntrypointComponentProvider,
    AllSongsComponentProvider,
    AlbumComponentProvider,
    ArtistsComponentProvider,
    FavouritesComponentProvider,
    PlaylistComponentProvider,
    AllPlaylistsComponentProvider,
    PlayerViewComponentProvider,
    RemoteSongsComponentProvider,
    ForegroundServiceComponentProvider,
    BitmapFlowModelLoaderFactoryComponentProvider {

    fun context(): Context

    fun playerInteractor(): PlayerInteractor

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun playerInteractor(playerInteractor: PlayerInteractor): Builder

        @BindsInstance
        fun gson(gson: Gson): Builder

        @BindsInstance
        fun okHttpClient(client: OkHttpClient): Builder

        fun build(): ApplicationComponent
    }
}

@Scope
annotation class AppScope

@Scope
annotation class MainActivityScope

@Scope
annotation class FragmentScope
