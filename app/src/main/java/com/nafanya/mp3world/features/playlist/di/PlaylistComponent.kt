package com.nafanya.mp3world.features.playlist.di

import com.nafanya.mp3world.features.playlist.immutable_playlist.ImmutablePlaylistFragment
import dagger.Subcomponent

@Subcomponent
interface PlaylistComponent {

    fun inject(immutablePlaylistFragment: ImmutablePlaylistFragment)
}
