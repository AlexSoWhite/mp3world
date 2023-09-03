package com.nafanya.mp3world.features.albums.di

import com.nafanya.mp3world.features.albums.view.AlbumListFragment
import dagger.Subcomponent

@Subcomponent
interface AlbumComponent {

    fun inject(albumListFragment: AlbumListFragment)
}

interface AlbumComponentProvider {

    val albumComponent: AlbumComponent
}
