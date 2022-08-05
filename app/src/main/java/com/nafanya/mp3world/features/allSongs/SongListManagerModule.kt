package com.nafanya.mp3world.features.allSongs

import com.nafanya.mp3world.core.listManagers.ListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface SongListManagerModule {

    @Binds
    @IntoSet
    fun bindSongListManager(songListManager: SongListManager): ListManager
}
