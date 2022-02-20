package com.nafanya.mp3world.viewmodel.listViewModels.artists

import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class ArtistListViewModel : ListViewModelInterface() {

    fun getData(callback: (List<Artist>) -> Unit) {
        pageState.value = PageState.IS_LOADED
        triggerTitle()
        callback(ArtistListManager.artists)
    }

    private fun triggerTitle() {
        title.value = "Исполнители (${ArtistListManager.artists.size})"
    }
}
