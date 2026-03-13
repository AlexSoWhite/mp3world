package com.nafanya.mp3world.domain.user_playlists.di

import com.nafanya.mp3world.presentation.user_playlists.view_playlists.AllPlaylistsFragment
import com.nafanya.mp3world.presentation.user_playlists.modify_playlist.ModifyPlaylistFragment
import com.nafanya.mp3world.presentation.user_playlists.mutable_playlist.MutablePlaylistFragment
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
