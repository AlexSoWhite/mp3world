package com.nafanya.mp3world.features.playlists.playlist.di

import com.nafanya.mp3world.features.playlists.playlist.view.AddSongToListActivity
import com.nafanya.mp3world.features.playlists.playlist.view.CurrentPlaylistDialogActivity
import com.nafanya.mp3world.features.playlists.playlist.view.PlaylistActivity
import dagger.Subcomponent

@Subcomponent(modules = [PlaylistModule::class])
interface PlaylistComponent {

    fun inject(playlistActivity: PlaylistActivity)
    fun inject(addSongToListActivity: AddSongToListActivity)
    fun inject(currentPlaylistDialogActivity: CurrentPlaylistDialogActivity)
}
