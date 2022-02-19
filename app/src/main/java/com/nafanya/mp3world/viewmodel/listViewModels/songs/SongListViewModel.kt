package com.nafanya.mp3world.viewmodel.listViewModels.songs

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class SongListViewModel : ListViewModelInterface() {

    val title: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private fun startLoading(query: String, callback: (Playlist) -> Unit) {
        pageState.value = PageState.IS_LOADING
        title.value = query
        Downloader.preLoad(query) { playlist ->
            playlist?.let {
                title.postValue("$query (${it.songList.size})")
                pageState.postValue(PageState.IS_LOADED)
                callback(playlist)
            }
        }
    }

    fun getData(callback: (Playlist) -> Unit) {
        when {
            query != null -> {
                startLoading(query!!) {
                    it.name = query!!
                    callback(it)
                }
            }
            playlist != null -> {
                pageState.postValue(PageState.IS_LOADED)
                title.value = playlist!!.name
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
        }

        fun newInstanceWithQuery(
            query: String? = null
        ) {
            this.query = query
        }
    }
}
