package com.nafanya.mp3world.features.all_songs.di

import com.nafanya.mp3world.features.all_songs.presentation.AllSongsFragment
import dagger.Subcomponent

@Subcomponent
interface AllSongsComponent {

    fun inject(allSongsFragment: AllSongsFragment)
}

interface AllSongsComponentProvider {

    val allSongsComponent: AllSongsComponent
}
