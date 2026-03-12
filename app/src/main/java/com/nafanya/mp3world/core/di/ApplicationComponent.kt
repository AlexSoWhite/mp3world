package com.nafanya.mp3world.core.di

import android.content.Context
import com.google.gson.Gson
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import com.nafanya.mp3world.core.di.view_model.ViewModelFactoryModule
import com.nafanya.mp3world.core.wrappers.di.BitmapFlowModelLoaderFactoryComponentProvider
import com.nafanya.mp3world.core.wrappers.di.WrappersModule
import com.nafanya.mp3world.domain.albums.di.AlbumComponentProvider
import com.nafanya.mp3world.domain.albums.di.AlbumModule
import com.nafanya.mp3world.presentation.user_playlists.di.AllPlaylistsComponentProvider
import com.nafanya.mp3world.presentation.user_playlists.di.AllPlaylistsModule
import com.nafanya.mp3world.domain.all_songs.di.AllSongsComponentProvider
import com.nafanya.mp3world.domain.all_songs.di.AllSongsModule
import com.nafanya.mp3world.domain.artists.di.ArtistsComponentProvider
import com.nafanya.mp3world.domain.artists.di.ArtistsModule
import com.nafanya.mp3world.data.downloading.di.DownloadModule
import com.nafanya.mp3world.presentation.entrypoint.di.EntrypointComponentProvider
import com.nafanya.mp3world.presentation.favourites.di.FavouritesModule
import com.nafanya.mp3world.presentation.favourites.di.FavouritesComponentProvider
import com.nafanya.mp3world.presentation.foreground_service.di.ForegroundServiceComponentProvider
import com.nafanya.mp3world.data.local_storage.di.LocalStorageModule
import com.nafanya.mp3world.data.media_store.di.MediaStoreModule
import com.nafanya.mp3world.presentation.player_view.di.PlayerViewComponentProvider
import com.nafanya.mp3world.presentation.playlist.di.PlaylistComponentProvider
import com.nafanya.mp3world.presentation.remote_songs.di.RemoteSongsComponentProvider
import com.nafanya.player.interactor.PlayerInteractor
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient

@Component(
    modules = [
        ViewModelFactoryModule::class,
        MediaStoreModule::class,
        DownloadModule::class,
        LocalStorageModule::class,
        AllSongsModule::class,
        AlbumModule::class,
        ArtistsModule::class,
        FavouritesModule::class,
        AllPlaylistsModule::class,
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

        @BindsInstance
        fun dispatchersProvider(dispatchersProvider: DispatchersProvider): Builder

        @BindsInstance
        fun applicationScope(scope: CoroutineScope): Builder

        fun build(): ApplicationComponent
    }
}

@Scope
annotation class AppScope

@Scope
annotation class MainActivityScope

@Scope
annotation class FragmentScope
