package com.nafanya.mp3world.presentation.remote_songs.di

import com.nafanya.mp3world.presentation.remote_songs.RemoteSongsFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        SongSearchersModule::class
    ]
)
interface RemoteSongsComponent {

    fun inject(remoteSongsFragment: RemoteSongsFragment)
}
