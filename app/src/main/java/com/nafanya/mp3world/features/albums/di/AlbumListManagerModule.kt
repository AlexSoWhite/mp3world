package com.nafanya.mp3world.features.albums.di

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.features.albums.AlbumListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface AlbumListManagerModule {

    @Binds
    @IntoSet
    fun bindAlbumListManager(albumListManager: AlbumListManager): ListManager
}
