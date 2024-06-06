package com.nafanya.mp3world.features.allSongs.di

import com.nafanya.mp3world.features.allSongs.view.AllSongsFragment
import dagger.Subcomponent

@Subcomponent
interface AllSongsComponent {

    fun inject(allSongsFragment: AllSongsFragment)
}

interface AllSongsComponentProvider {

    val allSongsComponent: AllSongsComponent
}
