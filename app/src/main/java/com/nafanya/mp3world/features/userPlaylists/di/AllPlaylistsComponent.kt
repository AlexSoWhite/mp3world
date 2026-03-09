package com.nafanya.mp3world.features.userPlaylists.di

import com.nafanya.mp3world.features.userPlaylists.allPlaylists.AllPlaylistsFragment
import com.nafanya.mp3world.features.userPlaylists.modifyPlaylist.ModifyPlaylistFragment
import com.nafanya.mp3world.features.userPlaylists.mutablePlaylist.MutablePlaylistFragment
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
