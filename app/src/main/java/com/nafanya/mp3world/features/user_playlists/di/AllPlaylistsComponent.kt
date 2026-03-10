package com.nafanya.mp3world.features.user_playlists.di

import com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.AllPlaylistsFragment
import com.nafanya.mp3world.features.user_playlists.presentation.modify_playlist.ModifyPlaylistFragment
import com.nafanya.mp3world.features.user_playlists.presentation.mutable_playlist.MutablePlaylistFragment
import dagger.Subcomponent

@Subcomponent
interface AllPlaylistsComponent {

    fun inject(allPlaylistsFragment: AllPlaylistsFragment)
    fun inject(modifyPlaylistFragment: ModifyPlaylistFragment)
    fun inject(mutablePlaylistFragment: MutablePlaylistFragment)
}

interface AllPlaylistsComponentProvider {

    val allPlaylistsComponent: AllPlaylistsComponent
}
