package com.nafanya.mp3world.features.albums.di

import com.nafanya.mp3world.features.albums.view.AlbumListActivity
import dagger.Subcomponent

@Subcomponent(modules = [AlbumModule::class])
interface AlbumComponent {

    fun inject(activity: AlbumListActivity)
}
