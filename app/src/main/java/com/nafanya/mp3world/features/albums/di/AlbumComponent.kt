package com.nafanya.mp3world.features.albums.di

import com.nafanya.mp3world.features.albums.view.AlbumListFragment
import dagger.Subcomponent

@Subcomponent(modules = [AlbumModule::class])
interface AlbumComponent {

    fun inject(albumListFragment: AlbumListFragment)
}
