package com.nafanya.mp3world.features.remote_songs.di

import com.nafanya.mp3world.features.remote_songs.presentation.RemoteSongsFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        SongSearchersModule::class
    ]
)
interface RemoteSongsComponent {

    fun inject(remoteSongsFragment: RemoteSongsFragment)
}
