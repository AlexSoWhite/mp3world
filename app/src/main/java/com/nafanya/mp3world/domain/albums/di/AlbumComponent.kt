package com.nafanya.mp3world.domain.albums.di

import com.nafanya.mp3world.presentation.albums.AlbumListFragment
import dagger.Subcomponent

@Subcomponent
interface AlbumComponent {

    fun inject(albumListFragment: AlbumListFragment)
}

interface AlbumComponentProvider {

    val albumComponent: AlbumComponent
}
