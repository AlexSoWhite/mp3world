package com.nafanya.mp3world.features.allPlaylists.di

import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.AllPlaylistsFragment
import com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist.ModifyPlaylistFragment
import com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist.MutablePlaylistFragment
import dagger.Subcomponent

@Subcomponent(modules = [AllPlaylistsModule::class])
interface AllPlaylistsComponent {

    fun inject(allPlaylistsFragment: AllPlaylistsFragment)
    fun inject(modifyPlaylistFragment: ModifyPlaylistFragment)
    fun inject(mutablePlaylistFragment: MutablePlaylistFragment)
}
