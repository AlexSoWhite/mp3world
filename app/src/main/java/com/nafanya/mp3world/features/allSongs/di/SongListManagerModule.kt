package com.nafanya.mp3world.features.allSongs.di

import com.nafanya.mp3world.core.listManagers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.allSongs.SongListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SongListManagerModule {

    @Binds
    @[IntoMap ListManagerKey(ALL_SONGS_LIST_MANAGER_KEY)]
    fun bind(songListManager: SongListManager): ListManager
}
