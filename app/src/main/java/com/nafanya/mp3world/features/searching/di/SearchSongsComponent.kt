package com.nafanya.mp3world.features.searching.di

import com.nafanya.mp3world.features.searching.view.SearchSongListActivity
import dagger.Subcomponent

@Subcomponent(modules = [SearchSongsModule::class])
interface SearchSongsComponent {

    fun inject(searchSongListActivity: SearchSongListActivity)
}
