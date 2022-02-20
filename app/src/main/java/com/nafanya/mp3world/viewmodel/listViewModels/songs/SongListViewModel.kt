package com.nafanya.mp3world.viewmodel.listViewModels.songs

import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class SongListViewModel : ListViewModelInterface() {

    private fun startLoading(query: String, callback: (Playlist) -> Unit) {
        Downloader.preLoad(query) { playlist ->
            playlist?.let {
                callback(playlist)
            }
        }
    }

    fun getData(callback: (Playlist) -> Unit) {
        when {
            query != null -> {
                pageState.value = PageState.IS_LOADING
                title.value = query
                startLoading(query!!) {
                    it.name = query!!
                    title.postValue("${it.name} (${it.songList.size})")
                    pageState.postValue(PageState.IS_LOADED)
                    callback(it)
                }
            }
            playlist != null -> {
                pageState.postValue(PageState.IS_LOADED)
                title.value = "${playlist!!.name} (${playlist!!.songList.size})"
                callback(playlist!!)
            }
            else -> {
                throw(Exception("song list must be initialized with query ot playlist"))
            }
        }
    }

    companion object {

        private var playlist: Playlist? = null
        private var query: String? = null

        fun newInstanceWithPlaylist(
            playlist: Playlist? = null
        ) {
            this.playlist = playlist
            this.query = null
        }

        fun newInstanceWithQuery(
            query: String? = null
        ) {
            this.query = query
            this.playlist = null
        }
    }
}
