package com.nafanya.mp3world.features.playlist.di

import com.nafanya.mp3world.features.playlist.immutablePlaylist.ImmutablePlaylistFragment
import com.nafanya.mp3world.features.playlist.view.CurrentPlaylistDialogActivity
import dagger.Subcomponent

@Subcomponent
interface PlaylistComponent {

    fun inject(currentPlaylistDialogActivity: CurrentPlaylistDialogActivity)
    fun inject(immutablePlaylistFragment: ImmutablePlaylistFragment)
}
