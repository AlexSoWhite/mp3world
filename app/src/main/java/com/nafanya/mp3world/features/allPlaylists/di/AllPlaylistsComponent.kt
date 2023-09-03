package com.nafanya.mp3world.features.allPlaylists.di

import com.nafanya.mp3world.features.allPlaylists.allPlaylists.AllPlaylistsFragment
import com.nafanya.mp3world.features.allPlaylists.modifyPlaylist.ModifyPlaylistFragment
import com.nafanya.mp3world.features.allPlaylists.mutablePlaylist.MutablePlaylistFragment
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
