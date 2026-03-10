package com.nafanya.mp3world.features.user_playlists.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.view_model.ViewModelKey
import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.list_managers.ListManagerKey
import com.nafanya.mp3world.core.list_managers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.features.user_playlists.domain.UserPlaylistInteractor
import com.nafanya.mp3world.features.user_playlists.domain.UserPlaylistInteractorImpl
import com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.AllPlaylistsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AllPlaylistsModule {

    @Binds
    @[IntoMap ViewModelKey(AllPlaylistsViewModel::class)]
    fun bindViewModel(allPlaylistsViewModel: AllPlaylistsViewModel): ViewModel

    @Binds
    @[IntoMap ListManagerKey(PLAYLIST_LIST_MANAGER_KEY)]
    fun bindIntoMap(playlistListManagerImpl: UserPlaylistInteractorImpl): PlaylistProvider

    @Binds
    fun bind(playlistListManagerImpl: UserPlaylistInteractorImpl): UserPlaylistInteractor
}
