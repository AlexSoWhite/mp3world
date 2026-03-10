package com.nafanya.mp3world.presentation.playlist.di

import com.nafanya.mp3world.presentation.playlist.immutable_playlist.ImmutablePlaylistFragment
import dagger.Subcomponent

@Subcomponent
interface PlaylistComponent {

    fun inject(immutablePlaylistFragment: ImmutablePlaylistFragment)
}
