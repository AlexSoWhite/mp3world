package com.nafanya.mp3world.features.albums.di

import com.nafanya.mp3world.core.listManagers.ALBUM_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.albums.AlbumListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AlbumListManagerModule {

    @Binds
    @[IntoMap ListManagerKey(ALBUM_LIST_MANAGER_KEY)]
    fun bind(albumListManager: AlbumListManager): ListManager
}
