package com.nafanya.mp3world.domain.all_songs.di

import com.nafanya.mp3world.presentation.all_songs.AllSongsFragment
import dagger.Subcomponent

@Subcomponent
interface AllSongsComponent {

    fun inject(allSongsFragment: AllSongsFragment)
}

interface AllSongsComponentProvider {

    val allSongsComponent: AllSongsComponent
}
