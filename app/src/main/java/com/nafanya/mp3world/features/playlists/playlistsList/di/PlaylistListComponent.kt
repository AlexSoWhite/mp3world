package com.nafanya.mp3world.features.playlists.playlistsList.di

import com.nafanya.mp3world.features.playlists.playlistsList.view.PlaylistListActivity
import dagger.Subcomponent

@Subcomponent(modules = [PlaylistListModule::class])
interface PlaylistListComponent {

    fun inject(playlistListActivity: PlaylistListActivity)
}
