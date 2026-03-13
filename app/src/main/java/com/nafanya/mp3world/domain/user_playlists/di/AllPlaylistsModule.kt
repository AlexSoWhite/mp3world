package com.nafanya.mp3world.domain.user_playlists.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.view_model.ViewModelKey
import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.list_managers.ListManagerKey
import com.nafanya.mp3world.core.list_managers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.domain.user_playlists.UserPlaylistsInteractor
import com.nafanya.mp3world.domain.user_playlists.UserPlaylistsInteractorImpl
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.AllPlaylistsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface AllPlaylistsModule {

    @Binds
    @[IntoMap ViewModelKey(AllPlaylistsViewModel::class)]
    fun bindViewModel(allPlaylistsViewModel: AllPlaylistsViewModel): ViewModel

    @Binds
    @Singleton
    @[IntoMap ListManagerKey(PLAYLIST_LIST_MANAGER_KEY)]
    fun bindIntoMap(playlistListManagerImpl: UserPlaylistsInteractorImpl): PlaylistProvider

    @Binds
    @Singleton
    fun bind(playlistListManagerImpl: UserPlaylistsInteractorImpl): UserPlaylistsInteractor
}
