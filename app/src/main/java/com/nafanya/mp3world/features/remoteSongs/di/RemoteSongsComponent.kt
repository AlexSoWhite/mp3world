package com.nafanya.mp3world.features.remoteSongs.di

import com.nafanya.mp3world.features.remoteSongs.presentation.RemoteSongsFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        SongSearchersModule::class
    ]
)
interface RemoteSongsComponent {

    fun inject(remoteSongsFragment: RemoteSongsFragment)
}
