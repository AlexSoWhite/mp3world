package com.nafanya.mp3world.domain.all_songs.di

import com.nafanya.mp3world.presentation.all_songs.AllSongsFragment
import com.nafanya.mp3world.presentation.song_list_views.action_dialogs.LocalSongBottomSheetDialog
import dagger.Subcomponent

@Subcomponent
interface AllSongsComponent {

    fun inject(allSongsFragment: AllSongsFragment)
    fun inject(allSongsFragment: LocalSongBottomSheetDialog)
}

interface AllSongsComponentProvider {

    val allSongsComponent: AllSongsComponent
}
