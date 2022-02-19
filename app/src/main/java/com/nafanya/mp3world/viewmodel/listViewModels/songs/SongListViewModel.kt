package com.nafanya.mp3world.viewmodel.listViewModels.songs

import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.PageState

class SongListViewModel : ListViewModelInterface() {

    fun startLoading(query: String, callback: (Playlist) -> Unit) {
        pageState.value = PageState.IS_LOADING
        Downloader.preLoad(query) { playlist ->
            playlist?.let {
                pageState.postValue(PageState.IS_LOADED)
                callback(playlist)
            }
        }
    }
}
